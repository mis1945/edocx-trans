/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.rmj.edocx.trans.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.stream.Stream;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.rmj.appdriver.iface.GEntity;

/**
 *
 * @author jef
 */
@Entity
@Table(name = "EDocSys_Detail")
public class UnitEDocxDetail implements Serializable, GEntity {
   private static final long serialVersionUID = 1L;
   @Id
   @Basic(optional = false)
   @Column(name = "sTransNox")
   private String sTransNox;
   @Column(name = "nEntryNox")
   private Integer nEntryNox;
   @Column(name = "sBarrCode")
   private String sBarrCode;
   @Column(name = "sBriefDsc")
   private String sBriefDsc;
   @Column(name = "sFileName")
   private String sFileName;
   @Column(name = "sRemarksx")
   private String sRemarksx;
   @Column(name = "sMD5Hashx")
   private String sMD5Hashx;
   @Column(name = "cImgeStat")
   private String cImgeStat;
   @Column(name = "sModified")
   private String sModified;
   @Basic(optional = false)
   @Column(name = "dModified")
   @Temporal(TemporalType.TIMESTAMP)
   private Date dModified;   

   public UnitEDocxDetail() {
      this.sBarrCode = "";
      this.sBriefDsc = "";
      this.sFileName = "";
      this.sRemarksx = "";
      this.sMD5Hashx = "";
      this.cImgeStat = "0";
      
      //set vector for fields/columns
      laColumns = new LinkedList();
      laColumns.add("sTransNox");
      laColumns.add("nEntryNox");
      laColumns.add("sBarrCode");
      laColumns.add("sBriefDsc");
      laColumns.add("sFileName");
      laColumns.add("sRemarksx");
      laColumns.add("sMD5Hashx");
      laColumns.add("cImgeStat");
      laColumns.add("sModified");
      laColumns.add("dModified");
   }

   public String getTransNox() {
      return sTransNox;
   }

   public void setTransNox(String sTransNox) {
      this.sTransNox = sTransNox;
   }

   public String getBarrCode() {
      return sBarrCode;
   }

   public void setBarrCode(String sBarrCode) {
      this.sBarrCode = sBarrCode;
   }

   public Integer getEntryNox() {
      return nEntryNox;
   }

   public void setEntryNox(Integer nEntryNox) {
      this.nEntryNox = nEntryNox;
   }

   public String getBriefDsc() {
      return sBriefDsc;
   }

   public void setBriefDsc(String sBriedDsc) {
      this.sBriefDsc = sBriedDsc;
   }
   
   public String getFileName() {
      return sFileName;
   }

   public void setFileName(String sFileName) {
      this.sFileName = sFileName;
   }
   
   public String getRemarksx() {
      return sRemarksx;
   }

   public void setRemarksx(String sRemarksx) {
      this.sRemarksx = sRemarksx;
   }
   
   public String getMD5Hashx() {
      return sMD5Hashx;
   }
   
   public void setMD5Hashx(String sMD5Hashx) {
      this.sMD5Hashx = sMD5Hashx;
   }
   
   public String getImgeStat() {
      return cImgeStat;
   }

   public void setImgeStat(String cImgeStat) {
      this.cImgeStat = cImgeStat;
   }
   
   public String getModifiedBy() {
      return sModified;
   }

   public void setModifiedBy(String sModified) {
      this.sModified = sModified;
   }

   public Date getDateModified() {
      return dModified;
   }

   public void setDateModified(Date dModified) {
      this.dModified = dModified;
   }
   
   @Override
   public int hashCode() {
      int hash = 0;
      hash += (sTransNox != null ? sTransNox.hashCode() : 0);
      return hash;
   }

   @Override
   public boolean equals(Object object) {
      // TODO: Warning - this method won't work in the case the id fields are not set
      if (!(object instanceof UnitEDocxDetail)) {
         return false;
      }
      UnitEDocxDetail other = (UnitEDocxDetail) object;
      if ((this.sTransNox == null && other.sTransNox != null) || (this.sTransNox != null && !this.sTransNox.equals(other.sTransNox))) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "org.rmj.edocx.trans.pojo.UnitEDocxDetail[sTransNox=" + sTransNox + ", nEntryNox=" + nEntryNox + "]";
   }

    public Object getValue(String fsColumn){
       int lnCol = getColumn(fsColumn);
       if(lnCol > 0){
          return getValue(lnCol);
       }
       else
         return null;
    }

    public Object getValue(int fnColumn) {
      switch(fnColumn){
         case 1:
            return this.sTransNox;
         case 2:
            return this.nEntryNox;
         case 3:
            return this.sBarrCode;
         case 4:
            return this.sBriefDsc;
         case 5:
            return this.sFileName;
         case 6:
            return this.sRemarksx;
         case 7:
            return this.sMD5Hashx;
         case 8:
            return this.cImgeStat;
         case 9:
            return this.sModified;
         case 10:
            return this.dModified;
         default:
            return null;
      }
   }

   public String getTable() {
      return "EDocSys_Detail";
   }

    public int getColumn(String fsCol) {
        return laColumns.indexOf(fsCol) + 1;
    }

    public String getColumn(int fnCol) {
       if(laColumns.size() < fnCol)
           return "";
       else
           return (String) laColumns.get(fnCol - 1);
    }

    public void setValue(String fsColumn, Object foValue){
       int lnCol = getColumn(fsColumn);
       if(lnCol > 0){
          setValue(lnCol, foValue);
       }
    }

   public void setValue(int fnColumn, Object foValue) {
      switch(fnColumn){
         case 1: 
             this.sTransNox = (String) foValue;
             break;
         case 2: 
             this.nEntryNox = (Integer) foValue;
             break;
         case 3: 
             this.sBarrCode = (String) foValue;
             break;
         case 4: 
             this.sBriefDsc = (String)foValue;
             break;
         case 5: 
             this.sFileName = (String)foValue;
             break;
         case 6: 
             this.sRemarksx = (String)foValue;
             break;
         case 7: 
             this.sMD5Hashx = (String)foValue;
             break;
         case 8: 
             this.sMD5Hashx = (String)foValue;
             break;
         case 9: 
             this.cImgeStat = (String)foValue;
             break;
         case 10:
             this.sModified = (String) foValue;
             break;
         case 11:
             this.dModified = (Date) foValue;
      }
   }
   
   public int getColumnCount() {
      return laColumns.size();
   }

    public void list(){
        Stream.of(laColumns).forEach(System.out::println);        
    }

   //Member Variables here
   LinkedList laColumns = null;

}
