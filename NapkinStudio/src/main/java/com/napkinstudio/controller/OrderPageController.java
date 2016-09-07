package com.napkinstudio.controller;


import com.napkinstudio.entity.*;
import com.napkinstudio.manager.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.*;


@Controller
public class OrderPageController {

    private int ordId;

    @Autowired
    private UserManager userManager;

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private StatusManager statusManager;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private UserOrderManager userOrderManager;

    @Autowired
    private StatusSAPStatusRoleManager statusSAPStatusRoleManager;

    @Autowired
    private SAPstatusManager sapStatusManager;

    @Autowired
    private ProgresBarFieldsManager progresBarFieldsManager;

    @Autowired
    private StatusChangeManager statusChangeManager;
    @Autowired
    private CommentsManager commentsManager;

    @ModelAttribute("user")
    public User user(Principal principal) {
        String login = principal.getName();
        User user = userManager.findByLogin(login);

        List<Role> roles = roleManager.findByUserId(user.getUserId());
        user.setRoles(roles);
        return user;
    }


//  @RequestMapping(value = "/orders/{orderId}")
//    @ModelAttribute("theOrder")
//    public Order order() {
//            Order order = new Order();
////        Order order = orderManager.findById(orderId);
////        Integer SSId = order.getSAPstatus().getId();
////        Integer roleId = user.getRoles().get(0).getId();
////
////        StatusSAPStatusRole statusSAPStatusRole;
////        try {
////            statusSAPStatusRole = statusSAPStatusRoleManager.findStatusByRoleIdAndSAPStatusId(roleId, SSId);
////            System.out.println(statusSAPStatusRole.getStatus().getName());
////            List<StatusSAPStatusRole> statusSAPStatusRolesList = new ArrayList<>();
////            statusSAPStatusRolesList.add(statusSAPStatusRole);
////            order.getSAPstatus().setStatusSAPStatuseRoles(statusSAPStatusRolesList);
////        } catch (NullPointerException e) {
////            e.printStackTrace(System.out);
////        }
//
//        return order;
//
//    }

    @RequestMapping(value = "/orders/{orderId}")
    public String goToOrders(Model model,@PathVariable("orderId") int orderId, @ModelAttribute("user") User user) {
        ordId = orderId;
        Order theOrder = orderManager.findById(orderId);
        Integer SSId = theOrder.getSAPstatus().getId();
        Integer roleId = user.getRoles().get(0).getId();
//        Map<String, Order> modelMap = new HashMap<>();
//        modelMap.put("theOrder",theOrder);
//        model.mergeAttributes(modelMap);

        StatusSAPStatusRole statusSAPStatusRole;
        try {
            statusSAPStatusRole = statusSAPStatusRoleManager.findStatusByRoleIdAndSAPStatusId(roleId, SSId);
            System.out.println(statusSAPStatusRole.getStatus().getName());
            List<StatusSAPStatusRole> statusSAPStatusRolesList = new ArrayList<>();
            statusSAPStatusRolesList.add(statusSAPStatusRole);
            theOrder.getSAPstatus().setStatusSAPStatuseRoles(statusSAPStatusRolesList);
        } catch (NullPointerException e) {
            e.printStackTrace(System.out);
        }

//        String login = principal.getName();
//        User user = userManager.findByLogin(login);
//        Order theOrder =orderManager.findById(orderId);

//        List<Role> roles = roleManager.findByUserId(user.getUserId());
//        List<UserOrder> userOrders = userOrderManager.findOrdersByUserId(user.getUserId());
//        Integer roleId = roles.get(0).getId();
//        Integer SSId = theOrder.getSAPstatus().getId();
//        System.out.println("role=" + roleId + "; SSId=" + SSId);
//        StatusSAPStatusRole statusSAPStatusRole;
//        try {
//            statusSAPStatusRole = statusSAPStatusRoleManager.findStatusByRoleIdAndSAPStatusId(roleId, SSId);
//            System.out.println(statusSAPStatusRole.getStatus().getName());
//            List<StatusSAPStatusRole> statusSAPStatusRolesList = new ArrayList<>();
//            statusSAPStatusRolesList.add(statusSAPStatusRole);
//            theOrder.getSAPstatus().setStatusSAPStatuseRoles(statusSAPStatusRolesList);
//        } catch (NullPointerException e) {
//            e.printStackTrace(System.out);
//        }
//        user.setRoles(roles);

//        List<ProgresBarFields> barFields = progresBarFieldsManager.findAll();
//        int orderId = theOrder.getOrderId();
//        int roleId = user.getRoles().get(0).getId();
        List<Object[]> barFields = progresBarFieldsManager.findBarByRolePVICheckReject(orderId, roleId, theOrder.getPVIcheckScen(), theOrder.getRejected());
//        System.out.println(barFields);
//        System.out.println(barFields.toString());
//        System.out.println(barFields.size());
        Date initDate = new Date();
        Date dateToComp = new Date();


        for (Iterator<Object[]> iterator = barFields.iterator(); iterator.hasNext(); ) {
            Object[] object = iterator.next();
            System.out.println(object[0] + "; " + object[1]);
////            get real initialization date
            if ((object[0] != null) && ((object[0].equals("Proof requested")) || (object[0].equals("Proof request set up")))) {
                if (object[1] instanceof java.util.Date) {
                    initDate = (java.util.Date) object[1];
                }
            }
//////            remove old data from orevious rounds
            if (object[1] instanceof java.util.Date) {
                dateToComp = (java.util.Date) object[1];
            }
            System.out.println(dateToComp + " compare to " + initDate);
            if (dateToComp.before(initDate)) {
                object[1] = null;
            }
////            correcting progresbar according to onhold
            if (theOrder.getSAPstatus().getId() == 9) {
//                System.out.println(object[1]);
                if (object[1] == null) {
                    iterator.remove();
                }
            } else {
                if (object[0].equals("On hold")) {
//                    System.out.println("rem");
                    iterator.remove();
                }
            }
        }

try {
    Map<Integer, List<Comments>> commentsMap = commentsManager.findCommentsbyOrderId(orderId);


    model.addAttribute("PVIComments", commentsMap.get(2));
    model.addAttribute("DeptorComments", commentsMap.get(1));
    model.addAttribute("DTPComments", commentsMap.get(4));
    model.addAttribute("CustomerComments", commentsMap.get(5));
    model.addAttribute("StampsManufacComments", commentsMap.get(6));
    model.addAttribute("ProductionComments", commentsMap.get(7));

}catch (Exception e){
    e.printStackTrace(System.out);
}
 //        model.addAttribute("user", user);
//        model.addAttribute("userOrders", userOrders);
        model.addAttribute("theOrder", theOrder);
        model.addAttribute("barFields", barFields);
        model.addAttribute("orderPviCheck",theOrder.getPVIcheckScen());

        return "orderpage";
    }


    //    @RequestMapping(value = "/changestatus/{orderId}/{answer}")
//    public String changeOrderStatus(Model model, @PathVariable int orderId, @PathVariable String answer, Principal principal) {
    @RequestMapping(value = "/changestatus/{orderId}/{answer}")
    public String changeOrderStatus(Model model, @PathVariable int orderId, @PathVariable String answer, Principal principal) {

        String login = principal.getName();
        System.out.println("/////////////////changestatus/////////////////");
        System.out.println(orderId);
        System.out.println(answer);
        User user = userManager.findByLogin(login);
        List<Role> roles = roleManager.findByUserId(user.getUserId());
//        user.setRoles(roles);
        Order theOrder =orderManager.findById(orderId);
        SAPstatus newSAPStatus;
        StatusChange statusChange =new StatusChange();
        statusChange.setDateTime(new Date());
        System.out.println(theOrder.getOrderId());
//__1
        if ((theOrder.getSapStatus().getId()==1||theOrder.getSapStatus().getId()==6)&&roles.get(0).getId()==2){
            if (answer.equals("yes")){
                newSAPStatus=sapStatusManager.findById(2);
                theOrder.setSAPstatus(newSAPStatus);
                theOrder.setRejected(false);
                orderManager.save(theOrder);
                statusChange.setOrder(theOrder);
                statusChange.setSAPstatus(newSAPStatus);
                statusChangeManager.save(statusChange);
            }
        }else
//__2
            if (theOrder.getSapStatus().getId()==2&&user.getRoles().get(0).getId()==4){
                if (answer.equals("yes")){
                    if (theOrder.getPVIcheckScen()){
                        newSAPStatus=sapStatusManager.findById(3);
                    } else{
                        newSAPStatus=sapStatusManager.findById(4);
                    }
                    if (theOrder.getApprovalBy().equals("Customer")&&theOrder.getDebCheckScen()){
                        theOrder.setProcessId((byte) 1);
                    }
                    theOrder.setSAPstatus(newSAPStatus);
                    orderManager.save(theOrder);
                    statusChange.setOrder(theOrder);
                    statusChange.setSAPstatus(newSAPStatus);
                    statusChangeManager.save(statusChange);
                }
            }else
//__3
                if (theOrder.getSapStatus().getId()==3&&user.getRoles().get(0).getId()==2&&theOrder.getPVIcheckScen()){
                    if (answer.equals("yes")){
                        newSAPStatus=sapStatusManager.findById(4);
                        theOrder.setSAPstatus(newSAPStatus);
                        theOrder.setRejected(false);
                        orderManager.save(theOrder);
                        statusChange.setOrder(theOrder);
                        statusChange.setSAPstatus(newSAPStatus);
                        statusChangeManager.save(statusChange);
                    } else if (answer.equals("no")){
                        discardOrders(theOrder,statusChange);
                    }

                }else
//__4
                    if (theOrder.getSapStatus().getId()==4&&user.getRoles().get(0).getId()==1&&theOrder.getApprovalBy().equals("Deptor")){
                        if (answer.equals("yes")){
                            approveOrders(theOrder,statusChange);
//                newSAPStatus=sapStatusManager.findById(7);
//                theOrder.setSAPstatus(newSAPStatus);
//                orderManager.save(theOrder);
//                statusChange.setOrder(theOrder);
//                statusChange.setSAPstatus(newSAPStatus);
//                statusChangeManager.save(statusChange);
                        } else if (answer.equals("no")){
                            discardOrders(theOrder,statusChange);
//                newSAPStatus=sapStatusManager.findById(6);
//                theOrder.setSAPstatus(newSAPStatus);
//                theOrder.setRejected(true);
//                orderManager.save(theOrder);
//                statusChange.setOrder(theOrder);
//                statusChange.setSAPstatus(newSAPStatus);
//                statusChangeManager.save(statusChange);
                        }

                    }else
//__5&7
                        if (theOrder.getSapStatus().getId()==4&&user.getRoles().get(0).getId()==5&&theOrder.getApprovalBy().equals("Customer")
                                &&(!theOrder.getDebCheckScen()||(theOrder.getDebCheckScen()&&theOrder.getProcessId()==2))){
                            if (answer.equals("yes")){
                                approveOrders(theOrder,statusChange);
                            } else if (answer.equals("no")){
                                theOrder.setProcessId((byte) 3);
                                orderManager.save(theOrder);
                            }

                        }else
//__6
                            if (theOrder.getSapStatus().getId()==4&&user.getRoles().get(0).getId()==1&&theOrder.getApprovalBy().equals("Customer")&&theOrder.getDebCheckScen()&&theOrder.getProcessId()==1){
                                if (answer.equals("yes")){
                                    theOrder.setProcessId((byte) 2);
                                    orderManager.save(theOrder);
                                } else if (answer.equals("no")){
                                    discardOrders(theOrder,statusChange);
                                }

                            }else
//__8
                                if (theOrder.getSapStatus().getId()==4&&user.getRoles().get(0).getId()==1&&theOrder.getApprovalBy().equals("Customer")&&theOrder.getProcessId()==3){
                                    if (answer.equals("yes")){
                                        approveOrders(theOrder,statusChange);
                                    } else if (answer.equals("no")){
                                        discardOrders(theOrder,statusChange);
                                    }

                                }else
//__9
                                    if (theOrder.getSapStatus().getId()==6&&user.getRoles().get(0).getId()==2){
                                        if (answer.equals("yes")){
//                TODO: Check what todo next
                                            newSAPStatus=sapStatusManager.findById(1);
                                            theOrder.setSAPstatus(newSAPStatus);
                                            orderManager.save(theOrder);
                                            statusChange.setOrder(theOrder);
                                            statusChange.setSAPstatus(newSAPStatus);
                                            statusChangeManager.save(statusChange);
                                        } else if (answer.equals("no")){
                                            newSAPStatus=sapStatusManager.findById(2);
                                            theOrder.setSAPstatus(newSAPStatus);
                                            orderManager.save(theOrder);
                                            statusChange.setOrder(theOrder);
                                            statusChange.setSAPstatus(newSAPStatus);
                                            statusChangeManager.save(statusChange);
                                        }
                                    }else

//__10
                                        if (theOrder.getSapStatus().getId()==5&&user.getRoles().get(0).getId()==2&&theOrder.getProcessId()==4){
                                            if (answer.equals("yes")){
                                                theOrder.setProcessId((byte) 5);
                                                orderManager.save(theOrder);
                                            }
                                        }else

//__11
                                            if (theOrder.getSapStatus().getId()==5&&user.getRoles().get(0).getId()==4&&theOrder.getProcessId()==5){
                                                if (answer.equals("yes")){
                                                    newSAPStatus=sapStatusManager.findById(7);
                                                    theOrder.setSAPstatus(newSAPStatus);
                                                    orderManager.save(theOrder);
                                                    statusChange.setOrder(theOrder);
                                                    statusChange.setSAPstatus(newSAPStatus);
                                                    statusChangeManager.save(statusChange);
                                                }
                                            }

        model.addAttribute("theOrder",theOrder);
        return "redirect:/orders/{orderId}";
    }




    // functions for internal use
    public void approveOrders(Order theOrder, StatusChange statusChange) {
        SAPstatus newSAPStatus=sapStatusManager.findById(7);
        theOrder.setSAPstatus(newSAPStatus);
        orderManager.save(theOrder);
        statusChange.setOrder(theOrder);
        statusChange.setSAPstatus(newSAPStatus);
        statusChangeManager.save(statusChange);
    }

    public void discardOrders(Order theOrder, StatusChange statusChange) {
        SAPstatus newSAPStatus=sapStatusManager.findById(6);
        theOrder.setSAPstatus(newSAPStatus);
        theOrder.setRejected(true);
        orderManager.save(theOrder);
        statusChange.setOrder(theOrder);
        statusChange.setSAPstatus(newSAPStatus);
        statusChangeManager.save(statusChange);
    }


    @RequestMapping(value = "/orders/addcoment")
    public String addComment () {

                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println(ordId);



        return "redirect:/orders/{orderId}";
    }

}
