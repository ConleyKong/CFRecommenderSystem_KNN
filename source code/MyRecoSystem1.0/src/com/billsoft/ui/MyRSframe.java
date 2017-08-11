package com.billsoft.ui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.billsoft.forge.Recommender;

public class MyRSframe {

	protected Shell shlMyrecomsystem;
	Recommender recom = new Recommender();
	JFileChooser fileChooser = null;
	private Text trainingFilePath;
	private Text itemAttributeFilePath;
	private Text resultFilePath;
	private Text status;
	private Text testFilePath;
	
	private String trainFile = "data/train.txt";
	private String testFile =  "data/test.txt";
	private String attributeFile = "data/itemAttribute.txt";
	private String outputFile = "data/result.txt";
	

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			MyRSframe window = new MyRSframe();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlMyrecomsystem.open();
		shlMyrecomsystem.layout();
		while (!shlMyrecomsystem.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlMyrecomsystem = new Shell();
		shlMyrecomsystem.setSize(450, 300);
		shlMyrecomsystem.setText("MyRecomSystem1.0");
		shlMyrecomsystem.setLayout(null);
		
		Button button = new Button(shlMyrecomsystem, SWT.NONE);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				//run the main application
				status.setText("第一步：正在从Training文件中载入数据，请稍等……");
				status.update();
				recom.constrcutTrain(trainFile);
				status.setText("第二步：正在从ItemAttribute文件中载入Item属性信息，请稍候……");
				status.update();
				recom.constructItemAttributeMatrix(attributeFile);
				status.setText("最后一步：对Test文件中给出的数据进行评分中……");
				status.update();
				recom.processQuery(testFile,outputFile);
				status.setText("恭喜您，推荐评分已经完成，请注意查看输出文件，谢谢！");
				status.update();
			}
		});
		button.setBounds(279, 219, 80, 27);
		button.setText("\u6267\u884C\u7A0B\u5E8F");
		
		Label label = new Label(shlMyrecomsystem, SWT.NONE);
		label.setBounds(17, 61, 61, 17);
		label.setText("\u8BAD\u7EC3\u6587\u4EF6\uFF1A");
		
		Label lblItem = new Label(shlMyrecomsystem, SWT.NONE);
		lblItem.setBounds(17, 90, 68, 17);
		lblItem.setText("Item\u6587\u4EF6\uFF1A");
		
		Label label_1 = new Label(shlMyrecomsystem, SWT.NONE);
		label_1.setBounds(17, 150, 61, 17);
		label_1.setText("\u8F93\u51FA\u6587\u4EF6\uFF1A");
		
		trainingFilePath = new Text(shlMyrecomsystem, SWT.BORDER);
		trainingFilePath.setBounds(86, 58, 152, 23);
		trainingFilePath.setText(trainFile);
		
		itemAttributeFilePath = new Text(shlMyrecomsystem, SWT.BORDER);
		itemAttributeFilePath.setBounds(86, 87, 152, 23);
		itemAttributeFilePath.setText(attributeFile);
		

		testFilePath = new Text(shlMyrecomsystem, SWT.BORDER);
		testFilePath.setBounds(86, 118, 152, 23);
		testFilePath.setText(testFile);
		
		resultFilePath = new Text(shlMyrecomsystem, SWT.BORDER);
		resultFilePath.setBounds(86, 147, 152, 23);
		resultFilePath.setText(outputFile);
		
		Button reSelectTrainingFile = new Button(shlMyrecomsystem, SWT.NONE);
		reSelectTrainingFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				//select an txt file to replace the train file
				fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                //设置对话框的标题：
                fileChooser.setDialogTitle("请选择您的train文件");
                String extj[] = { "txt"};
                FileNameExtensionFilter fileFilter = new FileNameExtensionFilter( "本地txt文件: *.txt",extj);
                fileChooser.setFileFilter(fileFilter);//设置文件后缀过滤器
                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String filePath = file.getAbsolutePath();
                    trainFile = filePath;
                    System.out.println("你选择的文件路径：:" +filePath);
                } else if (result == JFileChooser.CANCEL_OPTION) {
                    System.out.println("你没有选取文件");
                }
                trainingFilePath.setText(trainFile);
			}
		});
		reSelectTrainingFile.setBounds(264, 56, 108, 27);
		reSelectTrainingFile.setText("\u91CD\u9009\u8BAD\u7EC3\u6587\u4EF6");
		
		Button reSelectItemAttributeFile = new Button(shlMyrecomsystem, SWT.NONE);
		reSelectItemAttributeFile.setText("\u91CD\u9009Item\u6587\u4EF6");
		reSelectItemAttributeFile.setBounds(264, 85, 108, 27);
		reSelectItemAttributeFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				//select an txt file to replace the train file
				fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                //设置对话框的标题：
                fileChooser.setDialogTitle("请选择您的itemAttribute文件");
                String extj[] = { "txt"};
                FileNameExtensionFilter fileFilter = new FileNameExtensionFilter( "本地txt文件: *.txt",extj);
                fileChooser.setFileFilter(fileFilter);//设置文件后缀过滤器
                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String filePath = file.getAbsolutePath();
                    attributeFile = filePath;
                    System.out.println("你选择的文件路径：:" +filePath);
                } else if (result == JFileChooser.CANCEL_OPTION) {
                    System.out.println("你没有选取文件");
                }
                trainingFilePath.setText(attributeFile);
			}
		});
		
		Button reSelectTestFile = new Button(shlMyrecomsystem, SWT.NONE);
		reSelectTestFile.setText("\u91CD\u9009test\u6587\u4EF6");
		reSelectTestFile.setBounds(264, 112, 108, 27);
		reSelectTestFile.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				//select an txt file to replace the train file
				fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                //设置对话框的标题：
                fileChooser.setDialogTitle("请选择您的test文件");
                String extj[] = { "txt"};
                FileNameExtensionFilter fileFilter = new FileNameExtensionFilter( "本地txt文件: *.txt",extj);
                fileChooser.setFileFilter(fileFilter);//设置文件后缀过滤器
                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String filePath = file.getAbsolutePath();
                    testFile = filePath;
                    System.out.println("你选择的文件路径：:" +filePath);
                } else if (result == JFileChooser.CANCEL_OPTION) {
                    System.out.println("你没有选取文件");
                }
                trainingFilePath.setText(testFile);
			}
		});

		
		Button reSelectResultFilePath = new Button(shlMyrecomsystem, SWT.NONE);
		reSelectResultFilePath.setText("\u91CD\u9009\u8F93\u51FA\u6587\u4EF6\u8DEF\u5F84");
		reSelectResultFilePath.setBounds(264, 145, 108, 27);
		reSelectResultFilePath.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				//select an txt file to replace the train file
				fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                //设置对话框的标题：
                fileChooser.setDialogTitle("请选择您的test文件");
                String extj[] = { "txt"};
                FileNameExtensionFilter fileFilter = new FileNameExtensionFilter( "本地txt文件: *.txt",extj);
                fileChooser.setFileFilter(fileFilter);//设置文件后缀过滤器
                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    String filePath = file.getAbsolutePath();
                    outputFile = filePath;
                    System.out.println("你选择的文件路径：:" +filePath);
                } else if (result == JFileChooser.CANCEL_OPTION) {
                    System.out.println("你没有选取文件");
                }
                trainingFilePath.setText(outputFile);
			}
		});
		
		status = new Text(shlMyrecomsystem, SWT.BORDER);
		status.setEditable(false);
		status.setText("\u5F85\u547D\u2026\u2026");
		status.setBounds(62, 186, 310, 27);
		
		Label label_2 = new Label(shlMyrecomsystem, SWT.NONE);
		label_2.setBounds(17, 186, 41, 17);
		label_2.setText("\u72B6\u6001\uFF1A");
		
		Label lblitem = new Label(shlMyrecomsystem, SWT.NONE);
		lblitem.setBounds(23, 10, 389, 17);
		lblitem.setText("\u6B22\u8FCE\u4F7F\u7528\u6211\u4EEC\u7684\u63A8\u8350\u6A21\u62DF\u7CFB\u7EDF\uFF0C\u8BE5\u7CFB\u7EDF\u53EF\u4EE5\u6839\u636E\u8BAD\u7EC3\u6587\u4EF6\u4E2D\u7684\u7528\u6237\u548Citem");
		
		Label lbltestitem = new Label(shlMyrecomsystem, SWT.NONE);
		lbltestitem.setBounds(10, 28, 402, 17);
		lbltestitem.setText("\u6570\u636E\u63A8\u6D4B\u51FAtest\u6587\u4EF6\u4E2D\u7684\u5404\u4E2Aitem\u7684\u5F97\u5206\uFF0C\u8F93\u51FA\u5230\u7ED3\u679C\u6587\u4EF6\u4E2D\u3002");
		
		Label label_3 = new Label(shlMyrecomsystem, SWT.NONE);
		label_3.setBounds(17, 121, 61, 17);
		label_3.setText("\u6D4B\u8BD5\u6587\u4EF6\uFF1A");
		
		
		
	}
}
