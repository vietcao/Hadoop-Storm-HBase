package HBase;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;

import org.apache.hadoop.hbase.TableName;

import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;

import org.apache.hadoop.hbase.util.Bytes;

public class SuperTable{

   public static void main(String[] args) throws IOException {
	  /*
      // Instantiate Configuration class

      // Instaniate HBaseAdmin class
      
      // Instantiate table descriptor class

      // Add column families to table descriptor

      // Execute the table through admin

      // Instantiating HTable class
     
      // Repeat these steps as many times as necessary

	      // Instantiating Put class
              // Hint: Accepts a row name

      	      // Add values using add() method
              // Hints: Accepts column family name, qualifier/row name ,value

      // Save the table
	
      // Close table

      // Instantiate the Scan class
     
      // Scan the required columns

      // Get the scan result

      // Read values from scan result
      // Print scan result
 
      // Close the scanner
   
      // Htable closer
       */
	   Configuration con = HBaseConfiguration.create();
	   
	   HBaseAdmin admin = new HBaseAdmin(con);
	   
       HTableDescriptor tableDescriptor = new
	      HTableDescriptor(TableName.valueOf("powers"));
       
       tableDescriptor.addFamily(new HColumnDescriptor("personal"));
       tableDescriptor.addFamily(new HColumnDescriptor("professional"));
       
       admin.createTable(tableDescriptor);
       
       HTable hTable = new HTable(con, "powers");

       Put p = new Put(Bytes.toBytes("row1"));

       // adding values using add() method
       // accepts column family name, qualifier/row name ,value
       p.add(Bytes.toBytes("personal"),
       Bytes.toBytes("hero"),Bytes.toBytes("superman"));

       p.add(Bytes.toBytes("personal"),
       Bytes.toBytes("power"),Bytes.toBytes("strength"));

       p.add(Bytes.toBytes("professional"),Bytes.toBytes("name"),
       Bytes.toBytes("clark"));

       p.add(Bytes.toBytes("professional"),Bytes.toBytes("xp"),
       Bytes.toBytes("100"));

       // Saving the put Instance to the HTable.
       hTable.put(p);
       
       
       p = new Put(Bytes.toBytes("row2"));

       // adding values using add() method
       // accepts column family name, qualifier/row name ,value
       p.add(Bytes.toBytes("personal"),
       Bytes.toBytes("hero"),Bytes.toBytes("batman"));

       p.add(Bytes.toBytes("personal"),
       Bytes.toBytes("power"),Bytes.toBytes("money"));

       p.add(Bytes.toBytes("professional"),Bytes.toBytes("name"),
       Bytes.toBytes("bruce"));

       p.add(Bytes.toBytes("professional"),Bytes.toBytes("xp"),
       Bytes.toBytes("50"));

       // Saving the put Instance to the HTable.
       hTable.put(p);
       
       p = new Put(Bytes.toBytes("row3"));

       // adding values using add() method
       // accepts column family name, qualifier/row name ,value
       p.add(Bytes.toBytes("personal"),
       Bytes.toBytes("hero"),Bytes.toBytes("wolverine"));

       p.add(Bytes.toBytes("personal"),
       Bytes.toBytes("power"),Bytes.toBytes("healing"));

       p.add(Bytes.toBytes("professional"),Bytes.toBytes("name"),
       Bytes.toBytes("logan"));

       p.add(Bytes.toBytes("professional"),Bytes.toBytes("xp"),
       Bytes.toBytes("75"));

       // Saving the put Instance to the HTable.
       hTable.put(p);
       
       hTable.close();
       
       HTable table = new HTable(con, "powers");
       
       Scan scan = new Scan();
       
       scan.addColumn(Bytes.toBytes("personal"), Bytes.toBytes("hero"));
       
       ResultScanner scanner = table.getScanner(scan);
       for (Result result = scanner.next(); result != null; result = scanner.next())
    	      System.out.println("Found row : " + result);
       
       scanner.close();
       
   }
}

