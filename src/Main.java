
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;



public class Main extends JFrame implements KeyListener{

	//https://www.lawebdelprogramador.com/foros/Java/523120-Tamano-de-Un-resultSet.html
	private JTextField linea;
	private JTextArea resultado;
	private Connection conexion;
	
	public Main() {
		super("Cliente MySQL");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Border borde = BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(10, 10, 10, 10), 
				BorderFactory.createCompoundBorder(
						BorderFactory.createEtchedBorder(),
						BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JScrollPane(resultado = new JTextArea()), BorderLayout.CENTER);
		resultado.setFocusable(false);
		resultado.setEditable(false);
		resultado.setBorder(borde);
		resultado.setFont(new Font("Courier New", Font.PLAIN, 12));
		linea = new JTextField("select * from mysql.user");
		//linea = new JTextField("select * from trabajador");
		linea.setBorder(borde);
		linea.addKeyListener(this);
		getContentPane().add(linea, BorderLayout.SOUTH);
		getContentPane().setPreferredSize(new Dimension(900, 700));
		pack();
		setLocationRelativeTo(null);
		
		Properties propiedades = new Properties();
		propiedades.put("user", "root");
		propiedades.put("password", "pass");
		try {
			conexion = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", propiedades);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					System.exit(-1);
				}
				new Main().setVisible(true);				
			}
		});

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			try {
				Statement sentencia = conexion.createStatement();//crea  una sentencia
				if (sentencia.execute(linea.getText())) {
					
					ResultSet datos = sentencia.getResultSet(); //recoge los resultados de la sentencia
					ResultSetMetaData meta = datos.getMetaData();
					
					
					int columns = meta.getColumnCount();//devuelve el numero de columna
					
					
					System.out.println();
					String linea = "";
					for (int i=1; i<columns; i++) {
						int n = meta.getColumnDisplaySize(i);//coge la longitud
						String label = meta.getColumnLabel(i);//coge las columnas
						linea += String.format("| %-"+ n + "s ", label);//formato 
					}
					resultado.append(linea + "|\n");//añade la linea al resultado(JtextArea)
					
					
				
					
							
							
					
					while (datos.next()) {
						linea = "";
						for (int i=1; i<columns; i++) {//si la columna es de un tipo de dato ejemplo int la transforma en string
							int tipo = meta.getColumnType(i);
							String dato = "";
							switch (tipo) {
							case Types.INTEGER:
								dato = String.valueOf(datos.getInt(i));
								break;
							case Types.CHAR:
							case Types.VARCHAR:
								dato = datos.getString(i);
								break;
								//
							case Types.LONGNVARCHAR:
								dato=datos.getString(i);
								break;
							case Types.DOUBLE:
								dato=String.valueOf(datos.getDouble(i));
							case Types.DATE:
								dato=String.valueOf(datos.getDouble(i));
							}
							int n1 = meta.getColumnLabel(i).length();
							int n2 = meta.getColumnDisplaySize(i);
							linea += String.format("| %-" + (n1 > n2 ? n1 : n2) + "s ", dato);
						}
						resultado.append(linea + "|\n");
					}
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


}

