package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TicketDAO {
	public static ArrayList<Ticket> dbRemainTicketArrayList = new ArrayList<>();
	public static ArrayList<Ticket> dbSellTicketArrayList = new ArrayList<>();
	public static ArrayList<Integer> TicketPriceList = new ArrayList<>();
	public static ArrayList<Integer> remainTicketCountList = new ArrayList<>();
	public static ArrayList<Integer> sellTicketCountList = new ArrayList<>();
	
	public static ArrayList<Integer> quarterlyRevenueList = new ArrayList<>();

	public static int priceR = 0;
	public static int priceA = 0;
	public static int priceB = 0;

	public static int remainRCount = 0;
	public static int sellRCount = 0;

	public static int remainACount = 0;
	public static int remainBCount = 0;

	public static int sellACount = 0;
	public static int sellBCount = 0;

	public static int price = 0;
	
	public static int firstQuarter=0;
	public static int secondQuarter=0;
	public static int thirdQuarter=0;
	public static int fourthQuarter=0;
	
	
	

	// 1. �л� ��� �Լ�(��Ϲ�ư���� stuObList�� �������� DB���� �ִ´�)
	public static int insertTicketData(Ticket ticket) {
		// 1-1 �����ͺ��̽� Ticket���̺� ���� �Է��ϴ� ������
		StringBuffer insertTicket = new StringBuffer();
		insertTicket.append("insert into tickettbl ");
		insertTicket.append(
				"(performancecode, performancetitle, performancedate, grade, ticketname, price, customer, customerphone, selldate) ");
		insertTicket.append("values ");
		insertTicket.append("(?, ?, ?, ?, ?, ?, ?, ?, ?) ");

		// 1-2 �����ͺ��̽� Connection�� ������
		Connection con = null;

		// 1-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt = null;

		int count = 0;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(insertTicket.toString());
			// 1-4 �������� ���� Ticket �����͸� ����

			psmt.setString(1, ticket.getPerformanceCode());
			psmt.setString(2, ticket.getPerformanceTitle());
			psmt.setString(3, ticket.getPerformanceDate());
			psmt.setString(4, ticket.getGrade());
			psmt.setString(5, ticket.getTicketName());
			psmt.setInt(6, ticket.getPrice());
			psmt.setString(7, ticket.getCustomer());
			psmt.setString(8, ticket.getCustomerPhone());
			psmt.setString(9, ticket.getSellDate());

			// 1-5 ������ �����͸� ���������� ����(excuteUpdate() : �������� �����ؼ� ���̺� ������ �Ҷ� ����ϴ� ������)
			count = psmt.executeUpdate();
			if (count == 0) {
				MainController.callAlert("���� ���� : �����ͺ��̽� ���� ������ ����");
				return count;
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
		return count;
	}

	// 2. ���̺��� ���� Ƽ���� ��� �������� �Լ�
	public static ArrayList<Ticket> getSelectRemainTicketTotalData(String selectRemainTicket) {
		// 2-1 �����ͺ��̽��� ���� Ƽ�� ���ڵ带 ��� �������� ������
		String selectTicket = "select * from tickettbl where performancecode='" + selectRemainTicket
				+ "' and customer is null group by ticketname";
		// 2-2 �����ͺ��̽� Connection�� ������
		Connection con = null;
		// 2-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt = null;
		// 2-4 �������� �����ϰ� ���� �����;��� ���ڵ带 ������ ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(selectTicket);
			// 2-5 ������ �����͸� ���������� ����(excuteQuery()�� �����ؼ� ���̺��� �����ö� ����ϴ� ������)
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select ������ ����");
				return null;
			}
			Ticket ticket = null;
			dbRemainTicketArrayList.clear();
			while (rs.next()) {
				ticket = new Ticket(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getInt(6), rs.getString(7), rs.getString(8), rs.getString(9));
				dbRemainTicketArrayList.add(ticket);
			}

		}

		catch (SQLException e) {
			e.printStackTrace();
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
		return dbRemainTicketArrayList;
	}

	// 2-1. ���̺��� �Ǹŵ� Ƽ���� ��� �������� �Լ�
	public static ArrayList<Ticket> getSelectSellTicketTotalData(String selectSellTicket) {
		// 2-1-1 �����ͺ��̽��� ���� Ƽ�� ���ڵ带 ��� �������� ������
		String selectTicket = "select * from tickettbl where performancecode='" + selectSellTicket
				+ "' and customer is not null group by ticketname";
		// 2-1-2 �����ͺ��̽� Connection�� ������
		Connection con = null;
		// 2-1-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt = null;
		// 2-1-4 �������� �����ϰ� ���� �����;��� ���ڵ带 ������ ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(selectTicket);
			// 2-1-5 ������ �����͸� ���������� ����(excuteQuery()�� �����ؼ� ���̺��� �����ö� ����ϴ� ������)
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select ������ ����");
				return null;
			}
			Ticket ticket = null;
			dbSellTicketArrayList.clear();
			while (rs.next()) {
				ticket = new Ticket(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getInt(6), rs.getString(7), rs.getString(8), rs.getString(9));
				dbSellTicketArrayList.add(ticket);
			}
		}

		catch (SQLException e) {
			e.printStackTrace();
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
		return dbSellTicketArrayList;
	}

	// 2-2 ���̺��� ���õ� performanceCode�� Ƽ�� ������ �������� �Լ�
	public static ArrayList<Integer> getSelectedTicketPriceData(String selectedPerformanceCode) {
		// 2-1-1 �����ͺ��̽��� ���� Ƽ�� ���ڵ带 ��� �������� ������
		String selectTicket = "select (select price from tickettbl where grade='R' and performancecode='"
				+ selectedPerformanceCode
				+ "' group by grade) as rprice, (select price from tickettbl where grade='A' and performancecode='"
				+ selectedPerformanceCode
				+ "' group by grade) as aprice, (select price from tickettbl where grade='B' and performancecode='"
				+ selectedPerformanceCode + "' group by grade) as bprice;";
		// 2-1-2 �����ͺ��̽� Connection�� ������
		Connection con = null;
		// 2-1-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt = null;
		// 2-1-4 �������� �����ϰ� ���� �����;��� ���ڵ带 ������ ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(selectTicket);
			// 2-1-5 ������ �����͸� ���������� ����(excuteQuery()�� �����ؼ� ���̺��� �����ö� ����ϴ� ������)
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select ������ ����");
				return null;
			}
			while (rs.next()) {
				priceR = rs.getInt(1);
				priceA = rs.getInt(2);
				priceB = rs.getInt(3);
			}
			TicketPriceList.add(priceR);
			TicketPriceList.add(priceA);
			TicketPriceList.add(priceB);
		}

		catch (SQLException e) {
			e.printStackTrace();
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
		return TicketPriceList;
	}

	// 3. Ƽ�� ��ư ���̺�信�� ������ ���ڵ带 ����Ÿ���̽����� �����ϴ� �Լ�
	public static int deleteTicketData(String ticketname) {
		// 3-1 �����ͺ��̽��� �л����̺� ���ڵ带 �����ϴ� ������
		String deleteTicket = "delete from tickettbl where ticketname = ? ";
		// 2-2 �����ͺ��̽� Connection�� ������
		Connection con = null;
		// 2-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt = null;
		// 2-4 �������� �����ϰ� ���� �����;��� ���ڵ带 ������ ��ü
		int count = 0;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(deleteTicket);
			psmt.setString(1, ticketname);
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

	// 3-1. t1 ���̺�信�� ������ ���ڵ带 ����Ÿ���̽����� �����ϴ� �Լ�
	public static int deleteTicketDataFromT1TableView(String selectedPerformanceCode) {
		// 3-1 �����ͺ��̽��� �л����̺� ���ڵ带 �����ϴ� ������
		String deleteTicket = "delete from tickettbl where performancecode = ? ";
		// 2-2 �����ͺ��̽� Connection�� ������
		Connection con = null;
		// 2-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt = null;
		// 2-4 �������� �����ϰ� ���� �����;��� ���ڵ带 ������ ��ü
		int count = 0;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(deleteTicket);
			psmt.setString(1, selectedPerformanceCode);
			// 2-5 ������ �����͸� ���������� ����(excuteQuery()�� �����ؼ� ���̺��� �����ö� ����ϴ� ������)
			count = psmt.executeUpdate();
			if (count == 0) {
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

	// 4. ���̺�信�� �����ִ� Ƽ���� �� Ƽ������ �����ͺ��̽� ���̺� ����
	public static int ticketRemainToSell(Ticket ticketRemainToSell) {
		// 4-1 �����ͺ��̽� Ticket���̺��� �����ϴ� ������
		StringBuffer sellTicket = new StringBuffer();
		sellTicket.append("update tickettbl set ");
		sellTicket.append("customer=?, customerphone=?, selldate=? where ticketname=? ");
		// 4-2 �����ͺ��̽� Connection�� ������
		Connection con = null;
		// 4-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt = null;
		int count = 0;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(sellTicket.toString());
			// 4-4 �������� ���� Ticket �����͸� ����

			psmt.setString(1, ticketRemainToSell.getCustomer());
			psmt.setString(2, ticketRemainToSell.getCustomerPhone());
			psmt.setString(3, ticketRemainToSell.getSellDate());
			psmt.setString(4, ticketRemainToSell.getTicketName());

			// 4-5 ������ �����͸� ���������� ����(excuteUpdate() : �������� �����ؼ� ���̺� ������ �Ҷ� ����ϴ� ������)
			count = psmt.executeUpdate();
			if (count == 0) {
				MainController.callAlert("�������� : �����ͺ��̽� ���� ������ ����");
				return count;
			}
		} catch (SQLException e) {
			e.printStackTrace();
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

	// 4-1. ���̺�信�� �ȸ� Ƽ���� �����ִ� Ƽ������ �����ͺ��̽� ���̺� ����
	public static int ticketSellToRemain(Ticket ticketSellToRemain) {
		// 4-1-1 �����ͺ��̽� Ticket���̺��� �����ϴ� ������
		StringBuffer sellTicket = new StringBuffer();
		sellTicket.append("update tickettbl set ");
		sellTicket.append("customer=?, customerphone=?, selldate=? where ticketname=? ");
		// 4-1-2 �����ͺ��̽� Connection�� ������
		Connection con = null;
		// 4-1-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt = null;
		int count = 0;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(sellTicket.toString());
			// 4-1-4 �������� ���� Ticket �����͸� ����

			psmt.setString(1, ticketSellToRemain.getCustomer());
			psmt.setString(2, ticketSellToRemain.getCustomerPhone());
			psmt.setString(3, ticketSellToRemain.getSellDate());
			psmt.setString(4, ticketSellToRemain.getTicketName());

			// 4-1-5 ������ �����͸� ���������� ����(excuteUpdate() : �������� �����ؼ� ���̺� ������ �Ҷ� ����ϴ� ������)
			count = psmt.executeUpdate();
			if (count == 0) {
				MainController.callAlert("�������� : �����ͺ��̽� ���� ������ ����");
				return count;
			}
		} catch (SQLException e) {
			e.printStackTrace();
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

	// 5-1. ���õ� ������ Ƽ���� �ܿ������� �������� �Լ�
	public static ArrayList<Integer> remainTicketCount(String ticket) {
		// 2-1-1 �����ͺ��̽��� ���� Ƽ�� ���ڵ带 ��� �������� ������
		String calRRemainTicket = "select (select count(*) from tickettbl where grade='R' and performancecode='"
				+ ticket + "' and customer is null) as 'R', "
				+ "(select count(*) from tickettbl where grade='A' and performancecode='" + ticket
				+ "' and customer is null) as 'A', "
				+ "(select count(*) from tickettbl where grade='B' and performancecode='" + ticket
				+ "' and customer is null) as 'B'";
		// 2-1-2 �����ͺ��̽� Connection�� ������
		Connection con = null;
		// 2-1-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt = null;
		// 2-1-4 �������� �����ϰ� ���� �����;��� ���ڵ带 ������ ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(calRRemainTicket);
			// 2-1-5 ������ �����͸� ���������� ����(excuteQuery()�� �����ؼ� ���̺��� �����ö� ����ϴ� ������)
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select ������ ����");
				return null;
			}
			while (rs.next()) {
				remainRCount = rs.getInt(1);
				remainACount = rs.getInt(2);
				remainBCount = rs.getInt(3);

			}
			remainTicketCountList.add(remainRCount);
			remainTicketCountList.add(remainACount);
			remainTicketCountList.add(remainBCount);

		}

		catch (SQLException e) {
			e.printStackTrace();
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
		return remainTicketCountList;
	}

	// 6-1. ���õ� ������ Ƽ���� �ȸ������� �������� �Լ�
	public static ArrayList<Integer> sellTicketCount(String ticket) {
		// 2-1-1 �����ͺ��̽��� ���� Ƽ�� ���ڵ带 ��� �������� ������
		String calRSellTicket = "select (select count(*) from tickettbl where grade='R' and performancecode='" + ticket
				+ "' and customer is not null) as 'R', (select count(*) from tickettbl where grade='A' and performancecode='"
				+ ticket
				+ "' and customer is not null) as 'A', (select count(*) from tickettbl where grade='B' and performancecode='"
				+ ticket + "' and customer is not null) as 'B';";
		// 2-1-2 �����ͺ��̽� Connection�� ������
		Connection con = null;
		// 2-1-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt = null;
		// 2-1-4 �������� �����ϰ� ���� �����;��� ���ڵ带 ������ ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(calRSellTicket);
			// 2-1-5 ������ �����͸� ���������� ����(excuteQuery()�� �����ؼ� ���̺��� �����ö� ����ϴ� ������)
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select ������ ����");
				return null;
			}
			while (rs.next()) {
				sellRCount = rs.getInt(1);
				sellACount = rs.getInt(2);
				sellBCount = rs.getInt(3);

			}
			sellTicketCountList.add(sellRCount);
			sellTicketCountList.add(sellACount);
			sellTicketCountList.add(sellBCount);

		}

		catch (SQLException e) {
			e.printStackTrace();
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
		return sellTicketCountList;
	}

	// 6-2. ������ �б⺰ ������� �������� �Լ�
	public static ArrayList<Integer> sellTicketQuarterlyRevenue(String year) {
		// 2-1-1 �����ͺ��̽��� ���� Ƽ�� ���ڵ带 ��� �������� ������
		String quarterlyRevenue = "select " + 
				"(select sum(a.ftotal) from (select count(*)*price as ftotal from tickettbl where customer is not null and selldate<'"+year+"-04-01' and selldate>='"+year+"-01-01' group by ticketname) as a) as totalfirst, " + 
				"(select sum(b.stotal) from (select count(*)*price as stotal from tickettbl where customer is not null and selldate<'"+year+"-07-01' and selldate>='"+year+"-04-01' group by ticketname) as b) as totalsecond," + 
				"(select sum(c.ttotal) from (select count(*)*price as ttotal from tickettbl where customer is not null and selldate<'"+year+"-10-01' and selldate>='"+year+"-07-01' group by ticketname) as c) as totalthird, " + 
				"(select sum(d.ftotal) from (select count(*)*price as ftotal from tickettbl where customer is not null and selldate<'"+year+"-12-31' and selldate>='"+year+"-10-01' group by ticketname) as d) as totalfourth";
		// 2-1-2 �����ͺ��̽� Connection�� ������
		Connection con = null;
		// 2-1-3 �������� �����ؾ��� Statement�� ������ ��.
		PreparedStatement psmt = null;
		// 2-1-4 �������� �����ϰ� ���� �����;��� ���ڵ带 ������ ��ü
		ResultSet rs = null;
		try {
			con = DBUtility.getConnection();
			psmt = con.prepareStatement(quarterlyRevenue);
			// 2-1-5 ������ �����͸� ���������� ����(excuteQuery()�� �����ؼ� ���̺��� �����ö� ����ϴ� ������)
			rs = psmt.executeQuery();
			if (rs == null) {
				MainController.callAlert("select ���� : select ������ ����");
				return null;
			}
			while (rs.next()) {
				firstQuarter= rs.getInt(1);
				secondQuarter= rs.getInt(2);
				thirdQuarter = rs.getInt(3);
				fourthQuarter = rs.getInt(4);

			}
			quarterlyRevenueList.add(firstQuarter);
			quarterlyRevenueList.add(secondQuarter);
			quarterlyRevenueList.add(thirdQuarter);
			quarterlyRevenueList.add(fourthQuarter);

		}

		catch (SQLException e) {
			e.printStackTrace();
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
		return quarterlyRevenueList;
	}

}
