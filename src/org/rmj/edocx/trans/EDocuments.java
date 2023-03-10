/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.edocx.trans;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.rmj.appdriver.constants.TransactionStatus;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.iface.GEntity;
import org.rmj.appdriver.MiscUtil;
import org.rmj.appdriver.SQLUtil;
import org.rmj.edocx.trans.pojo.UnitEDocuments;
import org.rmj.edocx.trans.pojo.UnitEDocxMaster;
import org.rmj.edocx.trans.pojo.UnitEDocxDetail;

/**
 *
 * @author jef
 */
public class EDocuments {
   public Object newTransaction() {
      UnitEDocuments loOcc = new  UnitEDocuments();
      Connection loCon = poGRider.getConnection();

      if(psBranchCD.equals("")) {
         psBranchCD = poGRider.getBranchCode();
      }

      loOcc.getMaster().setTransNox(MiscUtil.getNextCode(loOcc.getMaster().getTable(), "sTransNox", true, loCon, psBranchCD));

      return loOcc;
   }

   public Object loadTransaction(String string) {
      UnitEDocuments loOcc = new UnitEDocuments();
      Connection loCon = poGRider.getConnection();

      //Load the master record
      loOcc.setMaster(loadMaster(loCon, string));
      
      //Load detail if no error was encountered during loadMaster();
      if(getErrMsg().isEmpty() || getMessage().isEmpty())
          loOcc.setDetail(loadDetail(loCon, string));
     
      //Close connection if this is not a parent class...
      if(!pbWithParnt)
         MiscUtil.close(loCon);      

     return loOcc;
   }

   public Object saveUpdate(Object foEntity, String fsTransNox) {
      UnitEDocuments loNewEnt = null;
      UnitEDocuments loResult = null;
      
      Connection loCon = poGRider.getConnection();

      // Check for the value of foEntity
      if (!(foEntity instanceof UnitEDocuments)) {
          setErrMsg("Invalid Entity Passed as Parameter");
          return foEntity;
      }
      
      // Typecast the Entity to this object
      loNewEnt = (UnitEDocuments) foEntity;
      
      if(loNewEnt.getMaster().getTranStat()== null){
          setErrMsg("Invalid transaction date detected!");
          return foEntity;
      }
      
      //TODO: Test the user rights in these area...
      if(!pbWithParnt)
         poGRider.beginTrans();
      
      //Save the master transaction 
      loResult.setMaster(saveMaster(loCon, loNewEnt, fsTransNox));

      //save detail if no error was encountered during saveMaster();
      if(getErrMsg().isEmpty() || getMessage().isEmpty())
          loResult.setDetail(saveDetail(loCon, loNewEnt, fsTransNox));


      if(!pbWithParnt){
         if(getErrMsg().isEmpty()){
            poGRider.commitTrans();
         }
         else
            poGRider.rollbackTrans();
      }

      return loResult;
   }

   public boolean deleteTransaction(String fsTransNox) {
      UnitEDocuments  loOcc = (UnitEDocuments) loadTransaction(fsTransNox);
      boolean lbResult = false;

      if(loOcc == null){
         setMessage("No record found!");
         return lbResult;
      }

      //TODO: Test the user rights in these area...

      StringBuilder lsSQL = new StringBuilder();
      lsSQL.append("DELETE FROM " + loOcc.getMaster().getTable());
      lsSQL.append(" WHERE sTransNox = " + SQLUtil.toSQL(fsTransNox));

      if(!pbWithParnt)
         poGRider.beginTrans();

      if(poGRider.executeQuery(lsSQL.toString(), loOcc.getMaster().getTable(), "", "") == 0){
         if(!poGRider.getErrMsg().isEmpty())
            setErrMsg(poGRider.getErrMsg());
         else
            setMessage("No record deleted");
      }
      else{
          lsSQL = new StringBuilder();
          lsSQL.append("DELETE FROM " + loOcc.getDetail().get(0).getTable());
          lsSQL.append(" WHERE sTransNox = " + SQLUtil.toSQL(fsTransNox));
          poGRider.executeQuery(lsSQL.toString(), loOcc.getDetail().get(0).getTable(), "", "");
         lbResult = true;
      }

      if(!pbWithParnt){
         if(getErrMsg().isEmpty()){
            poGRider.commitTrans();
         }
         else
            poGRider.rollbackTrans();
      }

      return lbResult;
   }

   public boolean closeTransaction(String string) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public boolean postTransaction(String string) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public boolean voidTransaction(String fsTransNox) {
      UnitEDocuments  loOcc = (UnitEDocuments) loadTransaction(fsTransNox);
      boolean lbResult = false;

      if(loOcc == null){
         setMessage("No record found!");
         return lbResult;
      }

      //TODO: Test the user rights in these area...
      //GCrypt loCrypt = new GCrypt();
      StringBuilder lsSQL = new StringBuilder();
      lsSQL.append("UPDATE " + loOcc.getMaster().getTable() + " SET ");
      lsSQL.append("  cTranStat = " + SQLUtil.toSQL(TransactionStatus.STATE_VOID));
      lsSQL.append(", sModified = " + SQLUtil.toSQL(psUserIDxx));
      lsSQL.append(", dModified = " + SQLUtil.toSQL(poGRider.getServerDate()));
      lsSQL.append(" WHERE sTransNox = " + SQLUtil.toSQL(fsTransNox));

      if(!pbWithParnt)
         poGRider.beginTrans();

      
      if(poGRider.executeQuery(lsSQL.toString(), loOcc.getMaster().getTable(), "", "") == 0){
         if(!poGRider.getErrMsg().isEmpty())
            setErrMsg(poGRider.getErrMsg());
         else
            setMessage("No record deleted");
      }
      else
         lbResult = true;

      //TODO: Write the update to the GCard here
      if(lbResult){

      }

      if(!pbWithParnt){
         if(getErrMsg().isEmpty()){
            poGRider.commitTrans();
         }
         else
            poGRider.rollbackTrans();
      }

      return lbResult;
   }

   public boolean cancelTransaction(String fsTransNox) {
      UnitEDocuments  loOcc = (UnitEDocuments) loadTransaction(fsTransNox);
      boolean lbResult = false;

      if(loOcc == null){
         setMessage("No record found!");
         return lbResult;
      }

      //TODO: Test the user rights in these area...
      //GCrypt loCrypt = new GCrypt();
      StringBuilder lsSQL = new StringBuilder();
      lsSQL.append("UPDATE " + loOcc.getMaster().getTable() + " SET ");
      lsSQL.append("  cTranStat = " + SQLUtil.toSQL(TransactionStatus.STATE_CANCELLED));
      lsSQL.append(", sModified = " + SQLUtil.toSQL(psUserIDxx));
      lsSQL.append(", dModified = " + SQLUtil.toSQL(poGRider.getServerDate()));
      lsSQL.append(" WHERE sTransNox = " + SQLUtil.toSQL(fsTransNox));

      if(!pbWithParnt)
         poGRider.beginTrans();

      if(poGRider.executeQuery(lsSQL.toString(), loOcc.getMaster().getTable(), "", "") == 0){
         if(!poGRider.getErrMsg().isEmpty())
            setErrMsg(poGRider.getErrMsg());
         else
            setMessage("No record deleted");
      }
      else
         lbResult = true;

      if(!pbWithParnt){
         if(getErrMsg().isEmpty()){
            poGRider.commitTrans();
         }
         else
            poGRider.rollbackTrans();
      }

      return lbResult;
    }

   public String getMessage() {
      return psWarnMsg;
   }

   public void setMessage(String fsMessage) {
      this.psWarnMsg = fsMessage;
   }

   public String getErrMsg() {
      return psErrMsgx;
   }

   public void setErrMsg(String fsErrMsg) {
      this.psErrMsgx = fsErrMsg;
   }

   public void setBranch(String foBranchCD) {
      this.psBranchCD = foBranchCD;
   }
   
   public void setOrigin(String foOriginxx) {
      this.psOriginxx = foOriginxx  ;
   }
   
   public void setDepartment(String foDeptIDxx) {
      this.psDeptIDxx = foDeptIDxx;
   }
   
   public void setEmployee(String foEmployID) {
      this.psEmployID = foEmployID;
   }
   
   public void setEDocSyFile(String foFileCode) {
      this.psFileCode = foFileCode;
   }

   public void setWithParent(boolean fbWithParent) {
      this.pbWithParnt = fbWithParent;
   }

   public String getSQ_Master() {
      return (MiscUtil.makeSelect(new UnitEDocxMaster()));
   }

   public String getSQ_Detail() {
      return (MiscUtil.makeSelect(new UnitEDocxDetail()));
   }
   
// add methods here
   public void setGRider(GRider foGRider) {
      this.poGRider = foGRider;
      this.psUserIDxx = foGRider.getUserID();
      if(psBranchCD.isEmpty())
         psBranchCD = poGRider.getBranchCode();
   }

   public Date getTranDate() {
      return poTransact;
   }

   public void setTranDate(Date date) {
      this.poTransact = date;
   }
   
   private UnitEDocxMaster loadMaster(Connection con, String string){
      UnitEDocxMaster loOcc = new UnitEDocxMaster(); 

      //retrieve the record
      StringBuilder lsSQL = new StringBuilder();
      lsSQL.append(getSQ_Master());
      lsSQL.append(" WHERE sTransNox = " + SQLUtil.toSQL(string));

      Statement loStmt = null;
      ResultSet loRS = null;
      try {
         loStmt = con.createStatement();
         loRS = loStmt.executeQuery(lsSQL.toString());

         if(!loRS.next())
             setMessage("No transaction Found!");
         else{
            //load each column to the entity
            for(int lnCol=1; lnCol<=loRS.getMetaData().getColumnCount(); lnCol++){
                loOcc.setValue(lnCol, loRS.getObject(lnCol));
            }
         }
      } catch (SQLException ex) {
         Logger.getLogger(EDocuments.class.getName()).log(Level.SEVERE, null, ex);
         setErrMsg(ex.getMessage());
      }
      finally{
         MiscUtil.close(loRS);
         MiscUtil.close(loStmt);
      }
      
      return loOcc;
   }
   
   private ArrayList<UnitEDocxDetail> loadDetail(Connection con, String string){
      ArrayList <UnitEDocxDetail> detail = new ArrayList<UnitEDocxDetail>();

      //retrieve the record
      StringBuilder lsSQL = new StringBuilder();
      lsSQL.append(getSQ_Detail());
      lsSQL.append(" WHERE sTransNox = " + SQLUtil.toSQL(string));

      Statement loStmt = null;
      ResultSet loRS = null;
      try {
         loStmt = con.createStatement();
         loRS = loStmt.executeQuery(lsSQL.toString());

         if(!loRS.next())
             setMessage("No transaction Found!");
         else{
             do{
                UnitEDocxDetail loOcc = new UnitEDocxDetail(); 
                 //load each column to the entity
                for(int lnCol=1; lnCol<=loRS.getMetaData().getColumnCount(); lnCol++){
                    loOcc.setValue(lnCol, loRS.getObject(lnCol));
                }
                detail.add(loOcc);
             }while(loRS.next());
         }
      } catch (SQLException ex) {
         Logger.getLogger(EDocuments.class.getName()).log(Level.SEVERE, null, ex);
         setErrMsg(ex.getMessage());
      }
      finally{
         MiscUtil.close(loRS);
         MiscUtil.close(loStmt);
      }
      
      return detail;
   }

   private UnitEDocxDetail loadDetail(Connection con, String string, String parts){
       UnitEDocxDetail detail = new UnitEDocxDetail();

      //retrieve the record
      StringBuilder lsSQL = new StringBuilder();
      lsSQL.append(getSQ_Detail());
      lsSQL.append(" WHERE sTransNox = " + SQLUtil.toSQL(string) +
                     " AND sPartsIDx = " + SQLUtil.toSQL(parts));

      Statement loStmt = null;
      ResultSet loRS = null;
      try {
         loStmt = con.createStatement();
         loRS = loStmt.executeQuery(lsSQL.toString());

         if(!loRS.next())
             setMessage("No transaction Found!");
         else{
             //load each column to the entity
            for(int lnCol=1; lnCol<=loRS.getMetaData().getColumnCount(); lnCol++){
                detail.setValue(lnCol, loRS.getObject(lnCol));
            }
         }
      } catch (SQLException ex) {
         Logger.getLogger(EDocuments.class.getName()).log(Level.SEVERE, null, ex);
         setErrMsg(ex.getMessage());
      }
      finally{
         MiscUtil.close(loRS);
         MiscUtil.close(loStmt);
      }
      
      return detail;
   }
   
   private UnitEDocxMaster saveMaster(Connection con, UnitEDocuments obj, String str){
      String lsSQL = "";
      UnitEDocxMaster loOcc = obj.getMaster();
      UnitEDocxMaster loOldEnt = new UnitEDocxMaster();

      //Set the value of sModified and dModified here
      //GCrypt loCrypt = new GCrypt();
      //loOcc.setModifiedBy(loCrypt.encrypt(psUserIDxx));
      loOcc.setModifiedBy(psUserIDxx);
      loOcc.setDateModified(poGRider.getServerDate());

      //Generate the SQL Statement
      if (str.equals("")) {
         loOcc.setValue(1, MiscUtil.getNextCode(loOcc.getTable(), "sTransNox", true, con, psBranchCD));

          lsSQL = MiscUtil.makeSQL((GEntity)loOcc);
      } else {
          //Reload previous record
          loOldEnt = (UnitEDocxMaster) loadTransaction(str);
          //Generate the UPDATE statement

          lsSQL = MiscUtil.makeSQL((GEntity)loOcc, (GEntity)loOldEnt,
                       "sTransNox = " + SQLUtil.toSQL(str));
      }

      //No changes has been made
      if (lsSQL.equals("")) {
          setMessage("Record is not updated!");
          return loOcc;
      }

      if(poGRider.executeQuery(lsSQL.toString(), loOcc.getTable(), "", "") == 0){
         if(!poGRider.getErrMsg().isEmpty())
            setErrMsg(poGRider.getErrMsg());
         else
            setMessage("No record updated");
      }
      
      return loOcc; 
   }
   
   private ArrayList<UnitEDocxDetail> saveDetail(Connection con, UnitEDocuments obj, String str){
      String lsSQL = "";
      ArrayList<UnitEDocxDetail> loOcc = obj.getDetail();
      ArrayList<UnitEDocxDetail> loNewEnt = new ArrayList<UnitEDocxDetail>();
      String lsTransNox = obj.getMaster().getTransNox();
      int lnCtr=0;
      
//      for(UnitEDocxDetail e: loOcc){
//          if(!e.getPartsID().isEmpty() && e.getPoints() > 0){
//              
//              lnCtr++;
//              e.setTransNo(lsTransNox);
//              e.setEntryNo(lnCtr);
//              if (str.equals("")) {
//                  lsSQL = MiscUtil.makeSQL((GEntity)loOcc);
//              }
//              else{
//                  UnitEDocxDetail f = (UnitEDocxDetail) loadDetail(con, str, e.getPartsID());
//                  //Generate the UPDATE statement
//
//                  lsSQL = MiscUtil.makeSQL((GEntity)e, (GEntity)f,
//                          "sTransNox = " + SQLUtil.toSQL(str) +
//                     " AND sPartsIDx = " + SQLUtil.toSQL(e.getPartsID()));                   
//              }
//              
//              if (!lsSQL.equals("")){
//                  if(poGRider.executeQuery(lsSQL.toString(), e.getTable(), "", "") == 0){
//                     if(!poGRider.getErrMsg().isEmpty())
//                        setErrMsg(poGRider.getErrMsg());
//                  }   
//              }
//              
//              loNewEnt.add(e);
//          }
//      }
      
      if(loNewEnt.isEmpty()){
          setMessage("No detail found!");
      }
      else{
        //If update then remove all items that are not in the current list of promo items   
//        if(!str.equals("")){
//           //Do we have items to remove?
//           lsSQL = "";
//           for(UnitEDocxDetail e: loNewEnt){
//               lsSQL = lsSQL + ", " + SQLUtil.toSQL(e.getPartsID());
//           }
//           
//           String lsSQL1 = "SELECT sPartsIDx " + 
//                  " FROM " + loNewEnt.get(0).getTable() +
//                  " WHERE sTransNox = " + SQLUtil.toSQL(str) +
//                    " AND sPartsIDx NOT IN(" + lsSQL.substring(1) + ")";
//           
//            Statement loStmt = null;
//            ResultSet loRS = null;
//            try {
//                loStmt = con.createStatement();
//                loRS = loStmt.executeQuery(lsSQL1.toString());
//
//                //Delete items if found
//                if(loRS.next()){
//                    lsSQL1 = "DELETE FROM" + loNewEnt.get(0).getTable() +
//                             " WHERE sTransNox = " + SQLUtil.toSQL(str) +
//                               " AND sPartsIDx NOT IN(" + lsSQL.substring(1) + ")";
//                
//                    poGRider.executeQuery(lsSQL.toString(), loNewEnt.get(0).getTable(), "", "");
//                }
//            } catch (SQLException ex) {
//                Logger.getLogger(EDocuments.class.getName()).log(Level.SEVERE, null, ex);
//                setErrMsg(ex.getMessage());
//            }
//            finally{
//                MiscUtil.close(loRS);
//                MiscUtil.close(loStmt);
//            }
//         }
     }
      
      return loNewEnt; 
   }   
   
   private boolean pbWithParnt = false;
   private String psBranchCD = "";
   private String psOriginxx = "";
   private String psDeptIDxx = "";
   private String psEmployID = "";
   private String psFileCode = "";
   private String psUserIDxx = "";
   private String psWarnMsg = "";
   private String psErrMsgx = "";
   private GRider poGRider = null;
   private Date poTransact = null;
}
