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
* @time 2019年8月23日 下午5:36:03
* @ClassName ...
* @Description 程序入口
*/
public class Start {
	public static void main(String[] args) {
		KingClassLoader kcl = new KingClassLoader();//自定义类加载器加载扩展包
		Domain.setClassLoader(kcl);
		Domain.getTree().setCellRenderer(new KingTreeCellRenderer());//设置脚本树的渲染器
		Domain.getResultTree().setCellRenderer(new ResultTreeCellRenderer());//设置运行结果树的渲染器
		
		JFrame frame = new JFrame("selenium工具");
		frame.setLayout(new BorderLayout());

		frame.setJMenuBar(new KingBar());
		frame.setContentPane(Domain.getRootPane());

		frame.addWindowListener(new WindowAdapter(){
			//窗口关闭事件
			@Override
			public void windowClosing(WindowEvent e){
				if(Domain.getChangeFile()){
					int code = JOptionPane.showConfirmDialog(frame, "文件尚未保存，是否要先保存？", "提示", JOptionPane.YES_NO_OPTION);
					if(code == JOptionPane.YES_OPTION){
						Domain.getToolBar().saveFile(false);
					}
				}
				Domain.closeDriver();
			}
		});
		//添加快捷键：Ctrl+s保存，Ctrl+o打开，Ctrl+n新建，Ctrl+c复制，Ctrl+v粘贴
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
		//在脚本运行的过程中，动态的更新结果树的UI,跨线程操作的解决方式
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
	
	//初始化JFrame，在电脑屏幕居中
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
