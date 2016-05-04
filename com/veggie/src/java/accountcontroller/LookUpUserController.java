package com.veggie.src.java.accountcontroller;

import com.veggie.src.java.Account;
import com.veggie.src.java.Controller;
import com.veggie.src.java.notification.Notification;
import com.veggie.src.java.form.Form;
import com.veggie.src.java.form.AbstractFormBuilder;
import com.veggie.src.java.form.AbstractFormBuilderFactory;
import com.veggie.src.java.database.AbstractDatabaseManagerFactory;
import com.veggie.src.java.database.AccountDatabaseManager;

import java.util.List;
import java.util.ArrayList;

public class LookUpUserController implements Controller {
   private Form lookUpForm;
   private Notification notification;
   private AccountDatabaseManager manager;

   public LookUpUserController() {
      notification = null;
      AbstractFormBuilder builder = AbstractFormBuilderFactory.getInstance().createFormBuilder();
      builder.addField("Library ID of User to Look Up");
      lookUpForm = builder.getResult();
      manager = AbstractDatabaseManagerFactory.getInstance().createAccountDatabaseManager();
   }

   /*public Form clickLookUpUser() {
      return lookUpForm;
   }*/

   public Form activate() { //Account lookUpUser(int idNum) {
      //Account user = manager.getUser(idNum);
      return lookUpForm;
   }

   public Notification getNotification(Form form) {
      lookUpForm = form;
      List<String> formData = lookUpForm.getData();
      Account user = manager.getUser(Integer.parseInt(formData.get(0)));
      return notification; //???
   }
}
