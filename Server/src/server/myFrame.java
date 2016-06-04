
package server;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;

/**
 *
 * @author Aynesh
 */
public class myFrame extends JFrame implements ActionListener{
    
 private DefaultListModel data;
 private Container con;
 private JList list;
 private FlowLayout Layout;
 private JLabel label1;
 private JButton button;
 private JTextField text;
 private JLabel label2;
 private startServer Server;
  myFrame() // the frame constructor method
  {
          super("Log Server");

    label1 = new JLabel();
    label2 = new JLabel();
    data=new DefaultListModel();
    button = new JButton();
    list = new JList(data);
    text = new JTextField("",22);
    button.setText("Start the server");
    FlowLayout grid =new FlowLayout();
    button.addActionListener(this);
    grid.setVgap(25);
    grid.setHgap(40);
    list.setVisible(true);
    this.add(label2);
    this.add(text);
    this.add(button);
    this.add(label1);
    this.add(list);
    list.setSize(200, 200);
    this.setLayout(grid);
    label1.setText("  The connected computers : ");
    label2.setText("  Enter log file extraction interval in ms : ");
    setBounds(100,100,500,500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
    setVisible(true); // display this frame
   

  }
  
    @Override
   public void actionPerformed(ActionEvent e) {
     String Interval;
     Interval=text.getText();
     if("".equals(Interval))
     {
        Interval="5000";
     }
     Server = new startServer(this,Integer.parseInt(Interval));
     Server.start();
     button.setEnabled(false);
     text.setEditable(false);
   }
  

    void addIPAddress(InetAddress inetAddress) {
            data.addElement("           "+ inetAddress+"           ");
    }
    
    void refreshList(InetAddress inetAddress)
    {
          data.removeElement("           "+inetAddress+"           ");
    }

}
