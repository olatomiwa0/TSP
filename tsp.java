import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

public class tsp 
{
	private JFrame frame;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					tsp window = new tsp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public tsp() 
	{
		initialize();
	}

	private void initialize() 
	{ 
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Arial", Font.PLAIN, 18));
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 630, 511);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBounds(0, 0, 614, 472);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JTextArea routeOutput = new JTextArea(10, 10);
		routeOutput.setEditable(false);
		routeOutput.setFont(new Font("Arial", Font.PLAIN, 16));
		routeOutput.setLineWrap(true);
		routeOutput.setBounds(442, 35, 159, 325);
		panel.add(routeOutput);
		
		JScrollPane addressSP = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		addressSP.setBounds(18, 371, 407, 87);
		panel.add(addressSP);
		
		JTextArea addressInput = new JTextArea();
		addressInput.setFont(new Font("Arial", Font.PLAIN, 14));
		addressSP.setViewportView(addressInput);
		
		JLabel solutionRoute = new JLabel("Route");
		solutionRoute.setFont(new Font("Arial", Font.BOLD, 16));
		solutionRoute.setBounds(495, 11, 46, 19);
		panel.add(solutionRoute);
		
		JLabel mapIcon = new JLabel("");
		mapIcon.setIcon(new ImageIcon("map.png"));
		mapIcon.setBounds(18, 35, 407, 314);
		panel.add(mapIcon);

		JLabel enterOrderLabel = new JLabel("Enter your orders below...");
		enterOrderLabel.setFont(new Font("Arial", Font.BOLD, 16));
		enterOrderLabel.setBounds(18, 357, 407, 14);
		panel.add(enterOrderLabel);

		JButton computeButton = new JButton("Compute");
		computeButton.setFont(new Font("Arial", Font.BOLD, 20));
		computeButton.setBounds(442, 371, 159, 87);
		panel.add(computeButton);
		computeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String addresses = addressInput.getText();
				String solution = compute(addresses);
				routeOutput.setText(solution);
			}
		});
	}

	public static String compute(String addresses)
	{
		List<String> orders = Arrays.asList(addresses.split("\n"));
		 
		double dMatrix [][] = new double[orders.size()][orders.size()]; //DISTANCE MATRIX 
		for(int i = 0; i < orders.size(); i++)
		{
			for(int j = 0; j < orders.size(); j++)
			{
				if(i == j) dMatrix[i][j] = 0;
				else{
					String [] ad1 = orders.get(i).split(",");
					double ad1lat = Double.parseDouble(ad1[3]);
					ad1lat = Math.toRadians(ad1lat);
					double ad1long = Double.parseDouble(ad1[4]);
					ad1long = Math.toRadians(ad1long);

					String [] ad2 = orders.get(j).split(",");
					double ad2lat = Double.parseDouble(ad2[3]);
					ad2lat = Math.toRadians(ad2lat);
					double ad2long = Double.parseDouble(ad2[4]);
					ad2long = Math.toRadians(ad2long);

					// DISTANCE FORMULA //
					double distance = 6371 * Math.acos(Math.sin(ad1lat) * Math.sin(ad2lat) + Math.cos(ad1lat) * Math.cos(ad2lat) * Math.cos(ad1long - ad2long));
					dMatrix[i][j] = distance; 
				}
			}
		}
	
		ArrayList<Integer> finalRoute = new ArrayList<Integer>();
		double apache = Double.MAX_VALUE;
		int firstOrder = 1;

		for(int i = 0; i < orders.size() ; i++)
		{
			//apache pizza coordinates
			double apalat = 53.38197;
			double apalong = -6.59274;

			String [] temp = orders.get(i).split(",");
			double ad1lat = Double.parseDouble(temp[3]);
			ad1lat = Math.toRadians(ad1lat);
			double ad1long = Double.parseDouble(temp[4]);
			ad1long = Math.toRadians(ad1long);

			double n = 6371 * Math.acos(Math.sin(apalat) * Math.sin(ad1lat) + Math.cos(apalat) * Math.cos(ad1lat) * Math.cos(apalong - ad1long));
			if(n < apache){
				apache = n;
				firstOrder = i;
			}
		}
		finalRoute.add(firstOrder);
		while(true)
		{
			double shortest = Double.MAX_VALUE;
			int order = 1;
			for(int j = 0; j < orders.size(); j++)
			{
				if(dMatrix[firstOrder][j] < shortest)
				{
					if(!finalRoute.contains(j) && firstOrder != j)
					{
						shortest = dMatrix[firstOrder][j];
						order = j;
					}
				}
			}
			finalRoute.add(order);
			firstOrder = order;		

			if(finalRoute.size() == orders.size()) break;
		}

		for(int i = 0; i < finalRoute.size(); i++)
		{
			int temp = finalRoute.get(i);
			finalRoute.set(i, temp + 1);
		}
		String [] temp = new String[finalRoute.size()];
		for(int i = 0; i < finalRoute.size(); i ++)
		{
			temp[i] = String.valueOf(finalRoute.get(i)); 
		}
		String route = routeToString(temp, ",");
		return route;
	}
	public static String routeToString(String[] temp, String a)
	{
		StringBuilder sb = new StringBuilder();
		for(String route : temp)
		{
			sb.append(route).append(a);
		}
		return sb.substring(0, sb.length() - 1);
	}
}
