package gsn.wrappers.general;

import gsn.beans.AddressBean;
import gsn.beans.DataField;
import gsn.beans.DataTypes;
import gsn.beans.StreamElement;
import gsn.wrappers.AbstractWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 * Links GSN to a Wisenet sensors network. The computer running this wrapper
 * should be connected to an IP network. One of the WSN nodes should forward
 * received packets through UDP to the host running this wrapper.
 */
public class UDPWrapper extends AbstractWrapper {

   private static final String       PACKET_SEPARATOR = System.getProperty("line.separator");
   private static final String    RAW_PACKET    = "RAW_PACKET";
   private static final Integer       DEFAULT_DATAGRAM_SIZE = 50;
   
   private final transient Logger logger        = Logger.getLogger( UDPWrapper.class );
   
   private int                    threadCounter = 0;
   private int                    datagramSize;
   
   public InputStream             is;
   
   private AddressBean            addressBean;
   
   private int                    port;
   
   private DatagramSocket         socket;
   
   /*
    * Needs the following information from XML file : port : the udp port it
    * should be listening to rate : time to sleep between each packet
    */
   public boolean initialize (  ) {
      addressBean = getActiveAddressBean( );
      try {
         port = Integer.parseInt( addressBean.getPredicateValue( "port" ) );
         socket = new DatagramSocket( port );
      } catch ( Exception e ) {
         logger.warn( e.getMessage( ) , e );
         return false;
      }
      datagramSize = Integer.parseInt( addressBean.getPredicateValueWithDefault("datagram-size", DEFAULT_DATAGRAM_SIZE.toString()) );
      setName( "UDPWrapper-Thread" + ( ++threadCounter ) );
      return true;
   }
   
   public void run ( ) {
      byte [ ] receivedData = new byte [ datagramSize ];
      DatagramPacket receivedPacket = null;
      String rest = "";
      while ( isActive( ) ) {
         try {
            receivedPacket = new DatagramPacket( receivedData , receivedData.length );
            socket.receive( receivedPacket );
            String data = new String(Arrays.copyOfRange(receivedPacket.getData(), receivedPacket.getOffset(), receivedPacket.getLength()));
            if ( logger.isDebugEnabled( ) ) logger.debug( "UDPWrapper received a packet : " + data );
            
            data = rest + data;
            
            String elements[] = data.split(PACKET_SEPARATOR);
            int stop = elements.length;
            rest = "";
            if (!data.endsWith(PACKET_SEPARATOR)) {
            	stop--;
            	rest = elements[elements.length-1];
            }
            
            for (int i=0; i<stop; i++) {
                StreamElement streamElement = new StreamElement( new String [ ] { RAW_PACKET } , new Byte [ ] { DataTypes.BINARY } , new Serializable [ ] { elements[i].getBytes() } , System
                      .currentTimeMillis( ) );
                postStreamElement( streamElement );
            }
         } catch ( IOException e ) {
            logger.warn( "Error while receiving data on UDP socket : " + e.getMessage( ) );
         }
      }
   }
   
   public  DataField [] getOutputFormat ( ) {
      return new DataField[] {new DataField( RAW_PACKET , "BINARY" , "The packet contains raw data received as a UDP packet." ) };
     
   }
   
   public void dispose (  ) {
	   socket.close();
	   threadCounter--;
   }
   public String getWrapperName() {
    return "network udp";
}
   public static void main ( String [ ] args ) {
   // To check if the wrapper works properly.
   // this method is not going to be used by the system.
   }
}
