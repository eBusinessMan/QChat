package lzx.qqclient.tools;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class TrayMenu extends PopupMenu implements ActionListener{
       private  MenuItem exit,find,alter,online,disappear;
       public TrayMenu()
        {
     	    exit=new MenuItem("�˳�");
     	    alter=new MenuItem("�޸�����");
		    find=new MenuItem("�һ�����");
            online=new MenuItem("��    ��");
		    disappear=new MenuItem("��    ��");
		    exit.addActionListener(this);
		    alter.addActionListener(this);
		    find.addActionListener(this);
		    online.addActionListener(this);
		    disappear.addActionListener(this);
		    this.add(online);
		    this.addSeparator();
		    this.add(disappear);
		    this.addSeparator();
		    this.add(alter);
		    this.addSeparator();
		    this.add(find);	 
		    this.addSeparator();
		    this.add(exit);	    
		    
        }
       public void actionPerformed(ActionEvent e)
       {
	      if(e.getSource()==alter)
	      {                       
	      }
	      if(e.getSource()==find)
	      {
                    
	      }
	       if(e.getSource()==exit)
	      {
	                      
	      }        
		  if(e.getSource()==online)
		  {

		  }
		   if(e.getSource()==disappear)
		  {
			 
		  }
	  } 
    }
     
