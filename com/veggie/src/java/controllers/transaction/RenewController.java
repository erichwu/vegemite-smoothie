package com.veggie.src.java.controllers.transaction;

import java.util.List;

import com.veggie.src.java.controllers.Controller;
import com.veggie.src.java.core.media.MediaItem;
import com.veggie.src.java.core.account.PatronAccount;
import com.veggie.src.java.form.Form;
import com.veggie.src.java.form.AbstractFormBuilder;
import com.veggie.src.java.form.AbstractFormBuilderFactory;
import com.veggie.src.java.notification.Notification;
import com.veggie.src.java.database.AbstractDatabaseManagerFactory;
import com.veggie.src.java.database.TransactionDatabaseManager;
import com.veggie.src.java.database.AccountDatabaseManager;
import com.veggie.src.java.database.ItemDatabaseManager;
import com.veggie.src.java.core.transactions.RentalTransaction;

public class RenewController implements Controller
{
  private AbstractFormBuilder formBuilder;
  private Form form;
  Notification notification;

  public RenewController()
  {
    formBuilder = AbstractFormBuilderFactory.getInstance().createFormBuilder();
    formBuilder.addField("User ID");
    formBuilder.addField("Item ID");
    form = formBuilder.getResult();
  }

  public Form activate()
  {
    return form;
  }

  public Notification submitForm()
  {
     List<String> formData = form.getData();
      int itemid;
      int userid;
      try{
      	userid = Integer.parseInt(formData.get(0));
      	itemid = Integer.parseInt(formData.get(1));
         TransactionDatabaseManager transactionDBManager = AbstractDatabaseManagerFactory.getInstance().createTransactionDatabaseManager();
      transactionDBManager.finalizeTransaction(itemid);
         AccountDatabaseManager accountDBManager = AbstractDatabaseManagerFactory.getInstance().createAccountDatabaseManager();
         PatronAccount patron;
         patron = (PatronAccount)accountDBManager.getUser(userid);
         ItemDatabaseManager itemDBManager = AbstractDatabaseManagerFactory.getInstance().createItemDatabaseManager();
         MediaItem med = itemDBManager.getItem(itemid);
        RentalTransaction rent = new RentalTransaction(patron, med, 0);
        transactionDBManager.addTransaction(rent);
      }catch(Exception e){
      	System.out.println("Error: incorrect form data");
      }

      return notification;   //???????

  }

}