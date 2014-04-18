package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.DBConnectionPool;
import vo.UserVo;

public class MysqlUserDao implements UserDao{
  DBConnectionPool dbConnectionPool;
  
  public void setDBConnectionPool(DBConnectionPool dbConnectionPool){
    this.dbConnectionPool = dbConnectionPool;
  }
  
  public void insert(UserVo user) throws Throwable{
    Connection con = null;
    PreparedStatement stmt = null;
    try{
      con = dbConnectionPool.getConnection();
      stmt = con.prepareStatement(
          "insert SE_USERS(EMAIL, PWD, NAME, TEL, FAX, POSTNO, ADDR)"
          + " values(?, ?, ?, ?, ?, ?, ?)");
      stmt.setString(1, user.getEmail());
      stmt.setString(2, user.getPwd());
      stmt.setString(3, user.getName());
      stmt.setString(4, user.getTel());
      stmt.setString(5, user.getFax());
      stmt.setString(6, user.getPostno());
      stmt.setString(7, user.getAddr());
      stmt.executeUpdate();
    }catch(Throwable e){
      throw e;
    } finally{
      try{stmt.close();}catch(Throwable e2){}
      dbConnectionPool.returnConnection(con);
    }
  }
  
  public List<UserVo> list(int pageNo, int pageSize) throws Throwable{
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try{
      con = dbConnectionPool.getConnection();
      stmt = con.prepareStatement("select UNO, EMAIL, NAME, TEL from SE_USERS"
          + " order by UNO desc limit ?, ?");
      stmt.setInt(1, (pageNo-1)*pageSize);
      stmt.setInt(2, pageSize);
      rs = stmt.executeQuery();
      ArrayList<UserVo> list = new ArrayList<UserVo>();
      while(rs.next()){
        list.add(new UserVo()
            .setNo(rs.getInt("UNO")).setName(rs.getString("NAME"))
            .setEmail(rs.getString("EMAIL")).setTel(rs.getString("TEL")));
      }
      return list;
    }catch(Throwable e){
      throw e;
    } finally{
      try{rs.close();}catch(Throwable e1){}
      try{stmt.close();}catch(Throwable e2){}
      dbConnectionPool.returnConnection(con);
    }
  }
  
  public UserVo detail(int no) throws Throwable{
    Connection con = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try{
      con = dbConnectionPool.getConnection();
      stmt = con.prepareStatement("select UNO, EMAIL, PWD, NAME, TEL, FAX,"
          + " POSTNO, ADDR from SE_USERS where UNO=?");
      stmt.setInt(1, no);
      rs = stmt.executeQuery();
      if(rs.next()){
        return new UserVo()
                    .setNo(rs.getInt("UNO"))
                    .setEmail(rs.getString("EMAIL"))
                    .setPwd(rs.getString("PWD"))
                    .setName(rs.getString("NAME"))
                    .setTel(rs.getString("TEL"))
                    .setFax(rs.getString("FAX"))
                    .setPostno(rs.getString("POSTNO"))
                    .setAddr(rs.getString("ADDR"));
      }else{
        throw new Exception("Can't find user");
      }
    }catch(Throwable e){
      throw e;
    } finally{
      try{rs.close();}catch(Throwable e1){}
      try{stmt.close();}catch(Throwable e2){}
      dbConnectionPool.returnConnection(con);
    }
  }
  
  public void update(UserVo user) throws Throwable{
    Connection con = null;
    PreparedStatement stmt = null;
    try{
      con = dbConnectionPool.getConnection();
      stmt = con.prepareStatement("update SE_USERS"
          + " set EMAIL=?, NAME=?, TEL=?, FAX=?, POSTNO=?, ADDR=?"
          + " where UNO=?");
      stmt.setString(1, user.getEmail());
      stmt.setString(2, user.getName());
      stmt.setString(3, user.getTel());
      stmt.setString(4, user.getFax());
      stmt.setString(5, user.getPostno());
      stmt.setString(6, user.getAddr());
      stmt.setInt(7, user.getNo());
      stmt.executeUpdate();
    }catch(Throwable e){
      throw e;
    } finally{
      try{stmt.close();}catch(Throwable e2){}
    }
  }
  
  public void delete(int no) throws Throwable{
    Connection con = null;
    PreparedStatement stmt = null;
    try{
      con = dbConnectionPool.getConnection();
      stmt = con.prepareStatement("delete from SE_USERS where UNO=?");
      stmt.setInt(1, no);
      stmt.executeUpdate();
    }catch(Throwable e){
      throw e;
    } finally{
      try{stmt.close();}catch(Throwable e2){}
      dbConnectionPool.returnConnection(con);
    }
  }
}