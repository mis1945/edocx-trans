/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.edocx.trans.pojo;

import java.util.ArrayList;
import javax.persistence.Column;

/**
 *
 * @author jef
 */
public class UnitEDocuments {
   UnitEDocxMaster master;
   ArrayList <UnitEDocxDetail> detail;

   public UnitEDocuments(){
      master = new UnitEDocxMaster();
      detail = new ArrayList<UnitEDocxDetail>();
   }

   public UnitEDocxMaster getMaster(){
      return master;
   }

   public ArrayList<UnitEDocxDetail> getDetail(){
      return detail;
   }

   public void setMaster(UnitEDocxMaster master){
      this.master = master;
   }

   public void setDetail(ArrayList<UnitEDocxDetail> detail){
      if(!this.detail.isEmpty())
         this.detail = new ArrayList<UnitEDocxDetail>();
      this.detail.addAll(detail);
   }
}
