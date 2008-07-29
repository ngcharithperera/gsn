package gsn.acquisition2;

import gsn.Main;
import gsn.acquisition2.messages.HelloMsg;
import gsn.acquisition2.wrappers.AbstractWrapper2;
import gsn.beans.AddressBean;
import gsn.wrappers.WrappersUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.mina.common.IoSession;

public class SafeStorage {
  
  public static final String SAFE_STORAGE_WRAPPERS_PROPERTIES = "conf/safe_storage_wrappers.properties";

  private static transient Logger                                logger                              = Logger.getLogger ( SafeStorage.class );
  
  private Properties wrappers;
  
  private SafeStorageDB storage ;
  
  public SafeStorage(int safeStoragePort) throws ClassNotFoundException, SQLException {

	  storage = new SafeStorageDB(safeStoragePort);		
	  wrappers = WrappersUtil.loadWrappers(new HashMap<String, Class<?>>(),SAFE_STORAGE_WRAPPERS_PROPERTIES);
	  storage.executeSQL("create table if not exists SETUP (pk INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY, table_name varchar not null unique, requester varchar not null unique,created_at TIMESTAMP default CURRENT_TIMESTAMP() not null )");
	  storage.executeSQL("create table if not exists HELLO (wrapper_id VARCHAR NOT NULL PRIMARY KEY, hellomsg OTHER NOT NULL)");

	  Iterator<HelloMsg> iter = getHelloMessages().iterator();
	  HelloMsg hello = null;
	  while (iter.hasNext()) {
		  hello = iter.next();
		  String wrapper_name = hello.getWrapperDetails().getPredicateValue("wrapper-name" );
		  logger.debug("Resuming the wrapper: " + wrapper_name + " for requester: " + hello.getRequster());
		  try {
			  prepareWrapper(hello, null);
		  } catch (InstantiationException e) {
			  logger.error(e.getMessage());
		  } catch (IllegalAccessException e) {
			  logger.error(e.getMessage());
		  }
	  }
  }
  
  public  Class < ? > getWrapperClass ( String id ) {
    try {
      String className =  wrappers.getProperty(id);
      if (className ==null) 
        logger.error("The requested wrapper: "+id+" doesn't exist in the "+SAFE_STORAGE_WRAPPERS_PROPERTIES+" file.");
      return Class.forName(className);  
    } catch (ClassNotFoundException e) {
      logger.error(e.getMessage(),e);
    }
    return null;
  }

  
  public AbstractWrapper2 prepareWrapper(HelloMsg helloMsg,IoSession network) throws InstantiationException, IllegalAccessException {
    AddressBean addressBean = helloMsg.getWrapperDetails();
    final String wrapper_name = addressBean.getPredicateValue("wrapper-name" );
    if ( wrappers.get  (wrapper_name) == null ) {
      logger.error ( "The wrapper >" + wrapper_name + "< is not defined in the >" + SAFE_STORAGE_WRAPPERS_PROPERTIES + "< file." );
      return null;
    }
    
    String wrapper_keep_processed_ss_entries = addressBean.getPredicateValue("wrapper-keep-processed-ss-entries");
    boolean keepProcessed = true;
    if (wrapper_keep_processed_ss_entries != null) {
    	keepProcessed = Boolean.parseBoolean(wrapper_keep_processed_ss_entries);
    }
    
    AbstractWrapper2 wrapper = ( AbstractWrapper2 )(getWrapperClass ( wrapper_name )).newInstance ( );
    if (wrapper ==null) {
      logger.error("The requested wrapper: "+wrapper_name+" doesn't exist.");
    }
    wrapper.setActiveAddressBean ( addressBean );
    boolean initializationResult = wrapper.initialize (  );
    if ( initializationResult == false ) {
       if (network != null) network.close();
       return null; 
    }
    try {
      String table_name = storage.prepareTableIfNeeded(helloMsg.getRequster());

      String wrapper_id = wrapper_name + table_name + helloMsg.getRequster();
      logger.warn("Saving wrapper: " + table_name + " for resume feature wrapper_id: " + wrapper_id);
      PreparedStatement psave = storage.createPreparedStatement("INSERT INTO hello VALUES (?,?)");
      PreparedStatement pis = storage.createPreparedStatement("SELECT * FROM hello where wrapper_id=?");

      pis.setString(1, wrapper_id);
      ResultSet rs = pis.executeQuery();
      if (! rs.next()) {
    	  psave.setString(1, wrapper_id);
    	  psave.setObject(2, helloMsg);
    	  psave.execute();
    	  psave.close();
      }
      pis.close();
      rs.close();
	  
      PreparedStatement ps = storage.createPreparedStatement("insert into "+table_name+" (stream_element) values (?)");
      wrapper.setTableName(table_name);
      wrapper.setNetwork(network);
      wrapper.setPreparedStatement(ps);
      wrapper.setKeepProcessedSafeStorageEntries(keepProcessed) ;
    } catch ( SQLException e ) {
      logger.error ( e.getMessage ( ) , e );
      return null;
    } 
    wrapper.start ( );
    return wrapper;
  }
  
  public ArrayList<HelloMsg> getHelloMessages () {
	  ArrayList<HelloMsg> helloMessages = new ArrayList<HelloMsg> () ;
	  ResultSet rs = null;
	  try {
		  PreparedStatement ps = storage.createPreparedStatement("select * from hello");
		  rs = ps.executeQuery();
		  while (rs.next()) {
			  Object hello = rs.getObject("hellomsg");
			  helloMessages.add((HelloMsg) hello);
		  }
		  rs.close();
		  ps.close();
	  } catch (SQLException e) {
		  e.printStackTrace();
	  }
	  return helloMessages;
  }
  
  public SafeStorageDB getStorage() {
    return storage;
  }
  
  
}
