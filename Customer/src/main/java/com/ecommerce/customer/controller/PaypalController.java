package com.ecommerce.customer.controller;

import com.ecommerce.customer.service.PaypalService;
//import com.paypal.api.payments.Order;
import com.ecommerce.library.model.Order;
import com.ecommerce.library.model.Pay;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@Controller
public class PaypalController {

    @Autowired
    private PaypalService service;

    public static final String SUCCESS_URL = "payment/success";
    public static final String CANCEL_URL = "payment/cancel";

    @PostMapping("/pay")
    public String payment(Model model, HttpSession session, @ModelAttribute("order") Pay order) {
        try {

            double subTotal = (double) session.getAttribute("subTotal");
            model.addAttribute("subTotal", subTotal);

            Payment payment = service.createPayment(subTotal, order.getCurrency(), order.getMethod(),
                    order.getIntent(), order.getDescription(), "http://localhost:8020/" + CANCEL_URL,
                    "http://localhost:8020/" + SUCCESS_URL);
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return "redirect:"+link.getHref();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "redirect:/check-out";
    }

    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return "paymentCancel";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return "paymentSuccess";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/check-out";
    }

}
