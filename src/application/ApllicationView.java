package application;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import javax.swing.JLabel;

public class ApllicationView extends JFrame {

	private static final long serialVersionUID = 6171506567231436384L;
	
	final ApplicationController controller = new ApplicationController();
	final ApplicationModel model = new ApplicationModel();

	public ApllicationView() {		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);

		JLabel lblNewLabel = new JLabel("Select example to execute");
		panel.add(lblNewLabel);

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);

		JButton btnNewButton = new JButton("Book Trading");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.executeScenario(model.bookTrading());				
				dispose();
			}
		});
		panel_1.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Employment");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.executeScenario(model.employment());
				dispose();
			}
		});
		panel_1.add(btnNewButton_1);

		JButton btnNewButton_2 = new JButton("Party");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.executeScenario(model.party());
				dispose();
			}
		});
		panel_1.add(btnNewButton_2);

		JButton btnNewButton_3 = new JButton("Yellow Pages");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.executeScenario(model.yellowPages());
				dispose();
			}
		});
		panel_1.add(btnNewButton_3);

		JButton btnNewButton_4 = new JButton("Contract Net");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.executeScenario(model.contractNet());
				dispose();
			}
		});
		panel_1.add(btnNewButton_4);

		JButton btnNewButton_5 = new JButton("Broker");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.executeScenario(model.broker());
				dispose();
			}
		});
		panel_1.add(btnNewButton_5);

		JButton btnNewButton_6 = new JButton("FIPA Request");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.executeScenario(model.fipaRequest());
				dispose();
			}
		});
		panel_1.add(btnNewButton_6);

		JButton btnNewButton_7 = new JButton("Time Server");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.executeScenario(model.timeServer());
				dispose();
			}
		});
		panel_1.add(btnNewButton_7);

		JButton btnNewButton_8 = new JButton("Thanks Agent");
		btnNewButton_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.executeScenario(model.thanksAgent());
				dispose();
			}
		});
		panel_1.add(btnNewButton_8);

		JButton btnNewButton_9 = new JButton("Pallete Robot");
		btnNewButton_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.executeScenario(model.palleteRobotCommunication());
				dispose();
			}
		});
		panel_1.add(btnNewButton_9);
		
		JButton btnNewButton_10 = new JButton("Conversation");
		btnNewButton_10.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				controller.executeScenario(model.conversation());
				dispose();
			}
		});
		panel_1.add(btnNewButton_10);
		
		JButton btnNewButton_11 = new JButton("Transport Line");
		btnNewButton_11.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				controller.executeScenario(model.transportline());
				dispose();
			}
		});
		panel_1.add(btnNewButton_11);

		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, BorderLayout.SOUTH);

		JLabel lblNewLabel_1 = new JLabel("Don't forget to turn off JADE through GUI");
		panel_2.add(lblNewLabel_1);
	}	
}
