//import java.util.HashMap;
//import java.util.Map;
//
//import com.stripe.Stripe;
//import com.stripe.exception.APIConnectionException;
//import com.stripe.exception.APIException;
//import com.stripe.exception.AuthenticationException;
//import com.stripe.exception.CardException;
//import com.stripe.exception.InvalidRequestException;
//import com.stripe.exception.StripeException;
//import com.stripe.model.Charge;
//import com.stripe.model.Customer;
//import com.travelerratings.server.util.Mailer;
//import com.travelerratings.shared.model.User;
//import com.travelerratings.shared.model.constants.UserSubscriptionPlanType;
//
//public class StripeHelper {
//
//	public static void main(String[] args) {
//		Stripe.apiKey = "sk_test_uxsK2BVCLlTQmsze6wdnL18H";
//
//		User user = new User();
//		user.setEmail("ssaammee@gmail.com");
//		user.setName("Sam Edwards");
//		user.setSubscriptionPlanType(UserSubscriptionPlanType.PLAN_0001);
//		String customerId = createCustomer(user);
//
//		// chargeCustomer(customerId);
//	}
//
//	private static void chargeCustomer(String customerId) {
//		String cardToken = "tok_1yU4tEJQb81E9E";
//		System.out.println("CustomerId - " + customerId);
//		Map<String, Object> chargeMap = new HashMap<String, Object>();
//		chargeMap.put("amount", 2900);
//		chargeMap.put("currency", "usd");
//		chargeMap.put("customer", customerId);
//		Map<String, Object> cardMap = new HashMap<String, Object>();
//		// cardMap.put("number", "4242424242424242");
//		// cardMap.put("exp_month", 12);
//		// cardMap.put("exp_year", 2020);
//		// chargeMap.put("card", cardMap);
//		try {
//			System.out.println("CALLING - CHARGE CREATE");
//			Charge charge = Charge.create(chargeMap);
//			System.out.println(charge);
//		} catch (StripeException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 
//	 * @param user The User To Create a Subscription For
//	 * @return Stripe Customer ID
//	 */
//	public static String createCustomer(User user) {
//		Customer customer = null;
//		Map<String, Object> customerParams = new HashMap<String, Object>();
//		customerParams.put("email", user.getEmail());
//		customerParams.put("description", "Licert.com - " + user.getEmail());
//		// customerParams.put("coupon", "coupon");
//		customerParams.put("plan", user.getSubscriptionPlanType());
//
//		try {
//			System.out.println("CALLING - CUSTOMER CREATE");
//			customer = Customer.create(customerParams);
//			System.out.println(customer);
//		} catch (AuthenticationException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (InvalidRequestException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (APIConnectionException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (CardException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (APIException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		return customer.getId();
//	}
//}