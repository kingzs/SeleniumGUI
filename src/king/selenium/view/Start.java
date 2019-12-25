package king.selenium.view;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import king.selenium.tools.KingClassLoader;
import king.selenium.view.left.KingTreeCellRenderer;
import king.selenium.view.left.ResultTreeCellRenderer;
import king.selenium.view.left.Tools;

/**
* @author King
* @email 281586342@qq.com
* @version v1.0
* @time 2019��8��23�� ����5:36:03
* @ClassName ...
* @Description �������
*/
public class Start {
	public static void main(String[] args) {
		KingClassLoader kcl = new KingClassLoader();//�Զ����������������չ��
		Domain.setClassLoader(kcl);
		Domain.getTree().setCellRenderer(new KingTreeCellRenderer());//���ýű�������Ⱦ��
		Domain.getResultTree().setCellRenderer(new ResultTreeCellRenderer());//�������н��������Ⱦ��
		
		JFrame frame = new JFrame("selenium����");
		frame.setLayout(new BorderLayout());

		frame.setJMenuBar(new KingBar());
		frame.setContentPane(Domain.getRootPane());

		frame.addWindowListener(new WindowAdapter(){
			//���ڹر��¼�
			@Override
			public void windowClosing(WindowEvent e){
				if(Domain.getChangeFile()){
					int code = JOptionPane.showConfirmDialog(frame, "�ļ���δ���棬�Ƿ�Ҫ�ȱ��棿", "��ʾ", JOptionPane.YES_NO_OPTION);
					if(code == JOptionPane.YES_OPTION){
						Domain.getToolBar().saveFile(false);
					}
				}
				Domain.closeDriver();
			}
		});
		//��ӿ�ݼ���Ctrl+s���棬Ctrl+o�򿪣�Ctrl+n�½���Ctrl+c���ƣ�Ctrl+vճ��
		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener(){
			private boolean down_ctrl;
			@Override
			public void eventDispatched(AWTEvent e) {
				if(e.getClass() == KeyEvent.class){
					KeyEvent keyEvent = (KeyEvent) e;
					if(keyEvent.getID() == KeyEvent.KEY_PRESSED){
						//System.out.println(keyEvent.getKeyCode());//s 83,o 79,n 78,c 67,v 86
						if(keyEvent.getKeyCode() == 17){
							down_ctrl = true;
						}else if(down_ctrl && keyEvent.getKeyCode() == 83){
							Domain.getToolBar().saveFile(true);
						}else if(down_ctrl && keyEvent.getKeyCode() == 79){
							Domain.getToolBar().openFile();
						}else if(down_ctrl && keyEvent.getKeyCode() == 78){
							Domain.getToolBar().newFile();
						}else if(down_ctrl && keyEvent.getKeyCode() == 67){
							Domain.setCopyData(Domain.getNode().getData());
						}else if(down_ctrl && keyEvent.getKeyCode() == 86){
							Tools.pasteData(null);
						}
					}else if(keyEvent.getID() == KeyEvent.KEY_RELEASED){
						if(keyEvent.getKeyCode() == 17){
							down_ctrl = false;
						}
					}
				}
			}
		}, AWTEvent.KEY_EVENT_MASK);
		
		initFrame(frame, 1000, 800);
		Domain.setChangeFile(false);
		//�ڽű����еĹ����У���̬�ĸ��½������UI,���̲߳����Ľ����ʽ
		while(true){
			synchronized(Domain.getResultTree()){
				try {
					Domain.getResultTree().wait();
					SwingUtilities.invokeLater(Domain.getResultTreeTask());
				} catch (InterruptedException e1) {
					continue;
				}
			}
		}
	}
	
	//��ʼ��JFrame���ڵ�����Ļ����
	public static void initFrame(JFrame frame, int width, int height){
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension d = toolkit.getScreenSize();
		int windowsWidth = (int) d.getWidth();
		int windowsHeight = (int) d.getHeight();
		frame.setBounds((windowsWidth-width)/2, (windowsHeight-height)/2, width, height);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
