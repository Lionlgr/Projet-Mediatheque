package mediatheque;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import documents.Abonne;
import documents.DVD;

public class mediatheque {
	private static List<DVD> dvd;
	private static List<Abonne> listeAbonnes;
	
	
	public mediatheque() {
		dvd = new ArrayList<DVD>();
		listeAbonnes = new ArrayList<Abonne>();
		
		String url = "jdbc:mysql://uuugnken96wmx2t4:xtAjJSuFtkHhOdC0jQtY@bbvveybpoylstuznizkg-mysql.services.clever-cloud.com:3306/bbvveybpoylstuznizkg";
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, "uuugnken96wmx2t4", "xtAjJSuFtkHhOdC0jQtY");
			ResultSet res = connection.createStatement().executeQuery("SELECT * FROM DVD");
			ResultSet res2 = connection.createStatement().executeQuery("SELECT * FROM Abonnes");
			while(res2.next()) {
				int id_Abonne = res2.getInt("id_Abonne") ;
				String nom = res2.getString("Nom") ;
				Date DateNaiss = res2.getDate("DateNaissance");
				Date DateFinInterdiction = res2.getDate("DateFinInterdiction");
				listeAbonnes.add(new Abonne(id_Abonne,nom,DateNaiss,DateFinInterdiction));
				
				System.out.format("%s %s %s\n", id_Abonne,nom,DateNaiss);
			}
			while(res.next()) {
				int id_DVD = res.getInt("id_DVD") ;
				String titre = res.getString("titre") ;
				boolean adulte = res.getBoolean("adulte");
				int id_Reserveur = res.getInt("id_Reserveur");
				int id_Emprunteur = res.getInt("id_Emprunteur");
				Abonne reserveur = mediatheque.chercherAbonne(id_Reserveur);
				Abonne emprunteur = mediatheque.chercherAbonne(id_Emprunteur);
				dvd.add(new DVD(id_DVD, titre, adulte, reserveur, emprunteur));
				
				System.out.format("%s %s %s\n", id_DVD, titre, adulte);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(connection !=null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		}
		
			
	}
	
	public static DVD chercherDVD(int id) {
		if(id != 0) {
			for(DVD i : dvd) {
				if(i.numero() == id) {
					return i;
				}
			}
		}
		return null;
	}
	public static Abonne chercherAbonne(int id) {
		if(id != 0) {
			
			for(Abonne i : listeAbonnes) {
				if(i.id() == id) {
					return i;
				}
			}
		}
		return null;
	}
	public static void modifierDVD(DVD dvd, Integer id_reservation, Integer id_emprunt) {
	    String url = "jdbc:mysql://uuugnken96wmx2t4:xtAjJSuFtkHhOdC0jQtY@bbvveybpoylstuznizkg-mysql.services.clever-cloud.com:3306/bbvveybpoylstuznizkg";
	    try (Connection conn = DriverManager.getConnection(url, "uuugnken96wmx2t4", "xtAjJSuFtkHhOdC0jQtY")) {
	        String sql = "UPDATE DVD SET id_Reserveur = ?, id_Emprunteur = ? WHERE id_DVD = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
	            if (id_reservation != null) {
	                stmt.setInt(1, id_reservation);
	            } else {
	                stmt.setNull(1, java.sql.Types.INTEGER);
	            }
	            if (id_emprunt != null) {
	                stmt.setInt(2, id_emprunt);
	            } else {
	                stmt.setNull(2, java.sql.Types.INTEGER);
	            }
	            stmt.setInt(3, dvd.numero());
	            stmt.executeUpdate();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	public static void ModifierBan(int id_abonne, java.util.Date dateFinInterdiction) {
	    String url = "jdbc:mysql://uuugnken96wmx2t4:xtAjJSuFtkHhOdC0jQtY@bbvveybpoylstuznizkg-mysql.services.clever-cloud.com:3306/bbvveybpoylstuznizkg";
	    try (Connection conn = DriverManager.getConnection(url, "uuugnken96wmx2t4", "xtAjJSuFtkHhOdC0jQtY")) {
	        String sql = "UPDATE Abonnes SET DateFinInterdiction = ?  WHERE id_Abonne = ?";
	        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setDate(1, new java.sql.Date(dateFinInterdiction.getTime()));
	            stmt.setInt(2, id_abonne);
	            stmt.executeUpdate();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	
	

}
