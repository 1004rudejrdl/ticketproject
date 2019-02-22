package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.PerTicket;
import Model.Performance;


public class PerformanceDAO {
	public static ArrayList<Performance> performanceDbArrayList = new ArrayList<>();
	public static ArrayList<PerTicket> perTicketDbArrayList = new ArrayList<>();
	public static ArrayList<PerTicket> perTicketSelectedTitleDbArrayList = new ArrayList<>();
	public static ArrayList<PerTicket> perTicketSelectedDateDbArrayList = new ArrayList<>();
	public static ArrayList<Performance> selectedPerformanceDbArrayList = new ArrayList<>();
	
	public static ArrayList<Integer> performanceGenreRatioDbArrayList = new ArrayList<>();
	public static ArrayList<Integer> performanceGenreRevenueDbArrayList = new ArrayList<>();
	
	public static int concert=0;
	public static int play=0;
	public static int musical=0;
	public static int exhibition=0;
	public static int festival=0;
	
	
	public static Performance t1selectedPerformance=null;
	// 1. �л� ��� �Լ�(��Ϲ�ư���� stuObList�� �������� DB���� �ִ´�)
	public static int insertPerformanceData(Performance performance) {
		//1-1 �����ͺ��̽� Performance���̺� ���� �Է��ϴ� ������
		StringBuffer insertPerformance= new StringBuffer();
		 insertPerformance.append("insert into performancetbl ");
		 insertPerformance.append("(performancecode, genre, performancetitle, location, performancedate, tag, runningtime, synopsis, casting, poster, produce, notice) ");
		 insertPerformance.append("values ");
		 insertPerformance.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
		
		//1-2 �����ͺ��̽� Connection�� ������
		Connection con = null;

		//1-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt=null;
		
		int count=0;
		try {
			con=DBUtility.getConnection();
			psmt=con.prepareStatement( insertPerformance.toString());
			//1-4 �������� ���� Performance �����͸� ����
			
			psmt.setString(1, performance.getPerformanceCode());
			psmt.setString(2, performance.getGenre());
			psmt.setString(3, performance.getPerformanceTitle());
			psmt.setString(4, performance.getLocation());
			psmt.setString(5, performance.getPerformanceDate());
			psmt.setString(6, performance.getTag());
			psmt.setString(7, performance.getRunningTime());
			psmt.setString(8, performance.getSynopsis());
			psmt.setString(9, performance.getCasting());
			psmt.setString(10, performance.getPoster());
			psmt.setString(11, performance.getProduce());
			psmt.setString(12, performance.getNotice());
			
			
			//1-5 ������ �����͸� ���������� ����(excuteUpdate() : �������� �����ؼ� ���̺� ������ �Ҷ� ����ϴ� ������)
			count = psmt.executeUpdate();
			if(count==0) {
				MainController.callAlert("���� ���� : �����ͺ��̽� ���� ������ ����");
				return count;
			}

		} catch (SQLException e) {
			MainController.callAlert("���� ���� : �����ͺ��̽��� ���� ����");
			e.printStackTrace();
		}finally{
			//1-6. �ڿ���ü�� �ݾ��ش�.
			try {
				if(psmt!=null) {psmt.close();}
				if(con!=null) {con.close();}
			}
			catch (SQLException e) {
				MainController.callAlert("�ڿ� �ݱ� ���� : ���� �ڿ� �ݱ� ����");
			}
		}
		return count;
	}

	// 2. ���̺��� �����ս� ��ü������ ��� �������� �Լ�
	public static ArrayList<Performance> getPerformanceTotalData() {
		// 2-1 �����ͺ��̽��� �л����̺� ���ڵ带 ��� �������� ������
		String selectPerformance = "select * from performancetbl";
		// 2-2 �����ͺ��̽� Connection�� ������
		Connection con = null;
		// 2-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt = null;
		// 2-4 �������� �����ϰ� ���� �����;��� ���ڵ带 ������ ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(selectPerformance);

			// 2-5 ������ �����͸� ���������� ����(excuteQuery()�� �����ؼ� ���̺��� �����ö� ����ϴ� ������)
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select ������ ����");
				return null;
			}
			while (rs.next()) {
				Performance performance = new Performance(rs.getString(1), rs.getString(3), rs.getString(2),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
						rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12));
				performanceDbArrayList.add(performance);
			}
		} catch (SQLException e) {
			MainController.callAlert("���� ���� : �����ͺ��̽��� ���� ����");
		} finally {
			// 1-6. �ڿ���ü�� �ݾ��ش�.
			try {
				if (psmt != null) {
					psmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				MainController.callAlert("�ڿ� �ݱ� ���� : ���� �ڿ� �ݱ� ����");
			}
		}
		return performanceDbArrayList;
	}
	// 2-1. ���̺��� perTicket ��ü������ ��� �������� �Լ�
	public static ArrayList<PerTicket> getPerTicketTotalData() {
		// 2-1 �����ͺ��̽��� �л����̺� ���ڵ带 ��� �������� ������
		String selectPerTicket = "select p.performancecode, p.genre, p.performancetitle, p.location, p.performancedate, p.tag, p.runningtime, (select count(*) from tickettbl as t where grade='R' and p.performancecode=t.performancecode and customer is null) as 'R', (select count(*) from tickettbl as t where grade='A' and p.performancecode=t.performancecode and customer is null) as 'A', (select count(*) from tickettbl as t where grade='B' and p.performancecode=t.performancecode and customer is null) as 'B' from performancetbl as p";
		// 2-2 �����ͺ��̽� Connection�� ������
		Connection con = null;
		// 2-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt = null;
		// 2-4 �������� �����ϰ� ���� �����;��� ���ڵ带 ������ ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(selectPerTicket);

			// 2-5 ������ �����͸� ���������� ����(excuteQuery()�� �����ؼ� ���̺��� �����ö� ����ϴ� ������)
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select ������ ����");
				return null;
			}
			while (rs.next()) {
				PerTicket perTicket = new PerTicket(
						rs.getString(1),
						rs.getString(2), 
						rs.getString(3), 
						rs.getString(4), 
						rs.getString(5), 
						rs.getString(6), 
						rs.getString(7), 
						rs.getInt(8), 
						rs.getInt(9),
						rs.getInt(10)
						);
				perTicketDbArrayList.add(perTicket);
			}
		} catch (SQLException e) {
			MainController.callAlert("���� ���� : �����ͺ��̽��� ���� ����");
		} finally {
			// 1-6. �ڿ���ü�� �ݾ��ش�.
			try {
				if (psmt != null) {
					psmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				MainController.callAlert("�ڿ� �ݱ� ���� : ���� �ڿ� �ݱ� ����");
			}
		}
		return perTicketDbArrayList;
	}
	// 2-2. ���̺��� perTicket ���� �˻� ������ ��� �������� �Լ�
	public static ArrayList<PerTicket> getPerTicketSelectedTitleData(String selectedPerformanceTitle) {
		// 2-1 �����ͺ��̽��� �л����̺� ���ڵ带 ��� �������� ������
		String selectPerTicket = "select p.performancecode, p.genre, p.performancetitle, p.location, p.performancedate, p.tag, p.runningtime, (select count(*) from tickettbl as t where grade='R' and p.performancecode=t.performancecode and customer is null) as 'R', (select count(*) from tickettbl as t where grade='A' and p.performancecode=t.performancecode and customer is null) as 'A', (select count(*) from tickettbl as t where grade='B' and p.performancecode=t.performancecode and customer is null) as 'B' from performancetbl as p where performancetitle='"+selectedPerformanceTitle+"'";
		// 2-2 �����ͺ��̽� Connection�� ������
		Connection con = null;
		// 2-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt = null;
		// 2-4 �������� �����ϰ� ���� �����;��� ���ڵ带 ������ ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(selectPerTicket);

			// 2-5 ������ �����͸� ���������� ����(excuteQuery()�� �����ؼ� ���̺��� �����ö� ����ϴ� ������)
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select ������ ����");
				return null;
			}
			while (rs.next()) {
				PerTicket perTicket = new PerTicket(
						rs.getString(1),
						rs.getString(2), 
						rs.getString(3), 
						rs.getString(4), 
						rs.getString(5), 
						rs.getString(6), 
						rs.getString(7), 
						rs.getInt(8), 
						rs.getInt(9),
						rs.getInt(10)
						);
				perTicketSelectedTitleDbArrayList.add(perTicket);
			}
		} catch (SQLException e) {
			MainController.callAlert("���� ���� : �����ͺ��̽��� ���� ����");
			e.printStackTrace();
		} finally {
			// 1-6. �ڿ���ü�� �ݾ��ش�.
			try {
				if (psmt != null) {
					psmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				MainController.callAlert("�ڿ� �ݱ� ���� : ���� �ڿ� �ݱ� ����");
			}
		}
		return perTicketSelectedTitleDbArrayList;
	}
	// 2-3. ���̺��� perTicket ��¥ �˻� ������ ��� �������� �Լ�
	public static ArrayList<PerTicket> getPerTicketSelectedDateData(String selectedPerformanceStartDate, String selectedPerformanceLastDate) {
		// 2-1 �����ͺ��̽��� �л����̺� ���ڵ带 ��� �������� ������
		String selectPerTicket = "select p.performancecode, p.genre, p.performancetitle, p.location, p.performancedate, p.tag, p.runningtime, (select count(*) from tickettbl as t where grade='R' and p.performancecode=t.performancecode and customer is null) as 'R', (select count(*) from tickettbl as t where grade='A' and p.performancecode=t.performancecode and customer is null) as 'A', (select count(*) from tickettbl as t where grade='B' and p.performancecode=t.performancecode and customer is null) as 'B' from performancetbl as p where performanceDate>='"+selectedPerformanceStartDate+"' and performancedate<='"+selectedPerformanceLastDate+"' ";   
		// 2-2 �����ͺ��̽� Connection�� ������
		Connection con = null;
		// 2-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt = null;
		// 2-4 �������� �����ϰ� ���� �����;��� ���ڵ带 ������ ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(selectPerTicket);

			// 2-5 ������ �����͸� ���������� ����(excuteQuery()�� �����ؼ� ���̺��� �����ö� ����ϴ� ������)
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select ������ ����");
				return null;
			}
			while (rs.next()) {
				PerTicket perTicket = new PerTicket(
						rs.getString(1),
						rs.getString(2), 
						rs.getString(3), 
						rs.getString(4), 
						rs.getString(5), 
						rs.getString(6), 
						rs.getString(7), 
						rs.getInt(8), 
						rs.getInt(9),
						rs.getInt(10)
						);
				perTicketSelectedDateDbArrayList.add(perTicket);
			}
		} catch (SQLException e) {
			MainController.callAlert("���� ���� : �����ͺ��̽��� ���� ����");
			e.printStackTrace();
		} finally {
			// 1-6. �ڿ���ü�� �ݾ��ش�.
			try {
				if (psmt != null) {
					psmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				MainController.callAlert("�ڿ� �ݱ� ���� : ���� �ڿ� �ݱ� ����");
			}
		}
		return perTicketSelectedDateDbArrayList;
	}

	// 2-4. t1���̺��� ���õ� �����ս� ��ü������ �������� �Լ�
	public static ArrayList<Performance> getPerformanceSelectedData(String performanceCode) {
		// 2-1 �����ͺ��̽��� �л����̺� ���ڵ带 ��� �������� ������
				String selectPerformance = "select * from performancetbl where performancecode='"+performanceCode+"'";
				// 2-2 �����ͺ��̽� Connection�� ������
				Connection con = null;
				// 2-3 �������� �����ؾ��� Statement�� ������ ��.
				PreparedStatement psmt = null;
				// 2-4 �������� �����ϰ� ���� �����;��� ���ڵ带 ������ ��ü
				ResultSet rs = null;
				try {
					con = DBUtility.getConnection();
					psmt = con.prepareStatement(selectPerformance);

					// 2-5 ������ �����͸� ���������� ����(excuteQuery()�� �����ؼ� ���̺��� �����ö� ����ϴ� ������)
					rs = psmt.executeQuery();
					if (rs == null) {
						MainController.callAlert("select ���� : select ������ ����");
						return null;
					}
					while (rs.next()) {
						Performance performance = new Performance(rs.getString(1), rs.getString(3), rs.getString(2),
								rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
								rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12));
						selectedPerformanceDbArrayList.add(performance);
					}
				} catch (SQLException e) {
					MainController.callAlert("���� ���� : �����ͺ��̽��� ���� ����");
				} finally {
					// 1-6. �ڿ���ü�� �ݾ��ش�.
					try {
						if (psmt != null) {
							psmt.close();
						}
						if (con != null) {
							con.close();
						}
					} catch (SQLException e) {
						MainController.callAlert("�ڿ� �ݱ� ���� : ���� �ڿ� �ݱ� ����");
					}
				}
				return selectedPerformanceDbArrayList;
	}
		
	// 2-5 t3piechart genre ratio �� �������� �Լ�
	public static ArrayList<Integer> getPerformanceGenreRatioData() {
		// 2-1 �����ͺ��̽��� �л����̺� ���ڵ带 ��� �������� ������
				String selectPerformance = "select (select count(*) from performancetbl where genre='concert') as concert, " + 
						"(select count(*) from performancetbl where genre='play') as play, " + 
						"(select count(*) from performancetbl where genre='musical') as musical, " + 
						"(select count(*) from performancetbl where genre='exhibition') as exhibition, " + 
						"(select count(*) from performancetbl where genre='festival') as festival";
				// 2-2 �����ͺ��̽� Connection�� ������
				Connection con = null;
				// 2-3 �������� �����ؾ��� Statement�� ������ ��.
				PreparedStatement psmt = null;
				// 2-4 �������� �����ϰ� ���� �����;��� ���ڵ带 ������ ��ü
				ResultSet rs = null;
				try {
					con = DBUtility.getConnection();
					psmt = con.prepareStatement(selectPerformance);

					// 2-5 ������ �����͸� ���������� ����(excuteQuery()�� �����ؼ� ���̺��� �����ö� ����ϴ� ������)
					rs = psmt.executeQuery();
					if (rs == null) {
						MainController.callAlert("select ���� : select ������ ����");
						return null;
					}
					while (rs.next()) {
						concert=rs.getInt(1);
						play=rs.getInt(2);
						musical=rs.getInt(3);
						exhibition=rs.getInt(4);
						festival=rs.getInt(5);
					}
					performanceGenreRatioDbArrayList.add(concert);
					performanceGenreRatioDbArrayList.add(play);
					performanceGenreRatioDbArrayList.add(musical);
					performanceGenreRatioDbArrayList.add(exhibition);
					performanceGenreRatioDbArrayList.add(festival);
				} catch (SQLException e) {
					MainController.callAlert("���� ���� : �����ͺ��̽��� ���� ����");
				} finally {
					// 1-6. �ڿ���ü�� �ݾ��ش�.
					try {
						if (psmt != null) {
							psmt.close();
						}
						if (con != null) {
							con.close();
						}
					} catch (SQLException e) {
						MainController.callAlert("�ڿ� �ݱ� ���� : ���� �ڿ� �ݱ� ����");
					}
				}
				return performanceGenreRatioDbArrayList;
	}

	// 2-6 t3BarChartGenre ���õ� �帣�� ���� �� �������� �Լ�
	public static ArrayList<Integer> getPerformanceGenreRevenue(String genre) {
		// 2-1 �����ͺ��̽��� �л����̺� ���ڵ带 ��� �������� ������
		performanceGenreRevenueDbArrayList.clear();
		String selectPerformance = "select sum(total.price) from (select genre, nt.ticketname, price from performancetbl as pt1 inner join" + 
				"	(select performancecode, ticketname, price from tickettbl where customer is not null group by ticketname) as nt" + 
				"   where nt.performancecode=pt1.performancecode and genre='"+genre+"') as total;";
		// 2-2 �����ͺ��̽� Connection�� ������
		Connection con = null;
		// 2-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt = null;
		// 2-4 �������� �����ϰ� ���� �����;��� ���ڵ带 ������ ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(selectPerformance);

			// 2-5 ������ �����͸� ���������� ����(excuteQuery()�� �����ؼ� ���̺��� �����ö� ����ϴ� ������)
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select ������ ����");
				return null;
			}
			while (rs.next()) {
				int genreRevenue = rs.getInt(1);
				performanceGenreRevenueDbArrayList.add(genreRevenue);
			}
		} catch (SQLException e) {
			MainController.callAlert("���� ���� : �����ͺ��̽��� ���� ����");
		} finally {
			// 1-6. �ڿ���ü�� �ݾ��ش�.
			try {
				if (psmt != null) {
					psmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				MainController.callAlert("�ڿ� �ݱ� ���� : ���� �ڿ� �ݱ� ����");
			}
		}
		return performanceGenreRevenueDbArrayList;
	}

	// 3. ���̺�信�� ������ ���ڵ带 ����Ÿ���̽����� �����ϴ� �Լ�
	public static int deletePerformanceData(String selectedPerformanceCode) {
		// 3-1 �����ͺ��̽��� �л����̺� ���ڵ带 �����ϴ� ������
		String deletePerformance = "delete from performancetbl where performancecode = ? ";
		// 2-2 �����ͺ��̽� Connection�� ������
		Connection con = null;
		// 2-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt = null;
		// 2-4 �������� �����ϰ� ���� �����;��� ���ڵ带 ������ ��ü
		int count = 0;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(deletePerformance);
			psmt.setString(1, selectedPerformanceCode);
			// 2-5 ������ �����͸� ���������� ����(excuteQuery()�� �����ؼ� ���̺��� �����ö� ����ϴ� ������)
			count = psmt.executeUpdate();
			if (count == 0) {
				MainController.callAlert("delete ���� : delete ������ ����");
				return count;
			}
		} catch (SQLException e) {
			MainController.callAlert("���� ���� : �����ͺ��̽��� ���� ����");
		} finally {
			// 1-6. �ڿ���ü�� �ݾ��ش�.
			try {
				if (psmt != null) {
					psmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				MainController.callAlert("�ڿ� �ݱ� ���� : ���� �ڿ� �ݱ� ����");
			}
		}
		return count;
	}

	// 4. ���̺�信�� ������ ���ڵ带 �����ͺ��̽� ���̺� ����
	public static int updatePerformanceData(String selectedPeformanceCode, Performance editSelectedPerformance) {
		StringBuffer editPerformance = new StringBuffer();
		editPerformance.append("update performancetbl set ");
		editPerformance.append("performancecode=?, genre=?, performancetitle=?, location=?, performancedate=?, tag=?, runningtime=?, synopsis=?, casting=?, poster=?, produce=?, notice=?   where performancecode=? ");
		//4-2 �����ͺ��̽� Connection�� ������
		Connection con = null;
		//4-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt=null;
		int count=0;
		try {
			con=DBUtility.getConnection();
			psmt=con.prepareStatement(editPerformance.toString());
			//4-4 �������� ���� Ticket �����͸� ����
		
			psmt.setString(1, editSelectedPerformance.getPerformanceCode());
			psmt.setString(2, editSelectedPerformance.getGenre());
			psmt.setString(3, editSelectedPerformance.getPerformanceTitle());
			psmt.setString(4, editSelectedPerformance.getLocation());
			psmt.setString(5, editSelectedPerformance.getPerformanceDate());
			psmt.setString(6, editSelectedPerformance.getTag());
			psmt.setString(7, editSelectedPerformance.getRunningTime());
			psmt.setString(8, editSelectedPerformance.getSynopsis());
			psmt.setString(9, editSelectedPerformance.getCasting());
			psmt.setString(10, editSelectedPerformance.getPoster());
			psmt.setString(11, editSelectedPerformance.getProduce());
			psmt.setString(12, editSelectedPerformance.getNotice());
			psmt.setString(13, selectedPeformanceCode);
			
			//4-5 ������ �����͸� ���������� ����(excuteUpdate() : �������� �����ؼ� ���̺� ������ �Ҷ� ����ϴ� ������)
			count = psmt.executeUpdate();
			if(count==0) {
				MainController.callAlert("�������� : �����ͺ��̽� ���� ������ ����");
				return count;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			MainController.callAlert("���� ���� : �����ͺ��̽��� ���� ����");
		}finally{
			//1-6. �ڿ���ü�� �ݾ��ش�.
			try {
				if(psmt!=null) {psmt.close();}
				if(con!=null) {con.close();}
			}
			catch (SQLException e) {
				MainController.callAlert("�ڿ� �ݱ� ���� : ���� �ڿ� �ݱ� ����");
			}
		}
		return count;
	}
	
}
