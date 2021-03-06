package com.github.CSCE4444ElectronicRestrauntSystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.LinkedList;
import java.util.List;

public class PayOrder extends AppCompatActivity {

    float total = 0.00f;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_order);

        // get order ID
        String orderID = getIntent().getExtras().getString("OrderID");

        // set title
        int orderNumber = getIntent().getExtras().getInt("OrderNumber");
        setTitle("Pay Order #" + (orderNumber + 1));

        // build query
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
        query.whereEqualTo("objectId", orderID);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject order, ParseException e) {
                //get order items
                LinkedList<OrderItem> orderItems = new LinkedList<>();
                List<Object> items = order.getList("ItemsOrdered");
                List<Object> requests = order.getList("Requests");
                for (Object item : items) {
                    // item name
                    String itemName = (String) item;

                    // request
                    if (!requests.isEmpty()) {
                        String request = (String) requests.remove(0);
                        orderItems.add(new OrderItem(itemName, request, 0f));
                    }
                }

                // set adapter
                ListView lvOrder = (ListView) findViewById(R.id.lvOrder);
                OrderAdapter adapter = new OrderAdapter(orderItems);
                lvOrder.setAdapter(adapter);


                //get adjustments
                TextView tvAdjustments = (TextView) findViewById(R.id.tvAdjustments);
                float adjustments = order.getNumber("Adjustments").floatValue();
                String formattedAdjustments = String.format("-$%.2f", adjustments);
                tvAdjustments.setText(formattedAdjustments);

                //get subtotal
                TextView tvSubTotal = (TextView) findViewById(R.id.tvSubTotal);
                float subTotal = order.getNumber("Total").floatValue();
                String formattedSubTotal = String.format("$%.2f", subTotal);
                tvSubTotal.setText(formattedSubTotal);

                //get tax
                TextView tvTax = (TextView) findViewById(R.id.tvTax);
                float tax = order.getNumber("Tax").floatValue();
                String formattedTax = String.format("$%.2f", tax);
                tvTax.setText(formattedTax);

                //get total
                TextView tvTotal = (TextView) findViewById(R.id.tvTotal);
                total = subTotal - adjustments + tax;
                String formattedTotal = String.format("$%.2f", total);
                tvTotal.setText(formattedTotal);
            }
        });
    }


    // pay cash button event
    public void payCash(View view) {
        final Intent i = getIntent();

        //Server Receives Customers Cash
        if(i.hasExtra("Server")){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
            query.whereEqualTo("objectId", i.getStringExtra("OrderID"));

            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject order, ParseException e) {
                    //get order items
                    order.put("Status", "Paid");
                    order.saveInBackground();
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }
        else {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Tables");
            MainApplication application = (MainApplication) getApplication();
            query.whereEqualTo("Number",application.currentTable);

            query.getFirstInBackground(new GetCallback<ParseObject>()
                 {
                        @Override public void done (ParseObject table, ParseException e){
                        table.put("Status", "Table Help");
                        table.put("Requests", "Customer requests to pay with cash.");
                        Toast.makeText(getApplicationContext(), "Server called.", Toast.LENGTH_LONG).show();
                        table.saveInBackground();
                        finish();
                        }
                 }
            );
        }
    }

    // pay credit button event
    public void payCredit(View view) {
        // get order number
        int orderNumber = getIntent().getExtras().getInt("OrderNumber");

        // get order id
        String orderID = getIntent().getExtras().getString("OrderID");

        // build intent
        Intent intent = new Intent(this, PayCredit.class);
        intent.putExtra("Total", total);
        intent.putExtra("OrderNumber", orderNumber);
        intent.putExtra("OrderID", orderID);

        startActivity(intent);
        finish();
    }

    // enter coupon button event
    public void rewardsClub(View view) {
        //Checks to see if the total is at least $10
        if(total < 10)
            Toast.makeText(getApplicationContext(), "Your meal must be at least $10", Toast.LENGTH_LONG).show();
        else {
            //Looks For Coupons
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Order");
            query.whereEqualTo("objectId", getIntent().getStringExtra("OrderID"));
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject order, ParseException e) {
                    //Checks to see if a coupon was used
                    if (order.getBoolean("Coupon"))
                        Toast.makeText(getApplicationContext(), "You have already Used a Coupon", Toast.LENGTH_LONG).show();
                    else {
                        //Goes to Rewards Login
                        String orderID = getIntent().getExtras().getString("OrderID");
                        Intent intent = new Intent(PayOrder.this, RewardsLogin.class);
                        intent.putExtra("Paying", true);
                        intent.putExtra("objectId", orderID);
                        intent.putExtra("OrderNumber", getIntent().getExtras().getInt("OrderNumber"));
                        intent.putExtra("OrderID", getIntent().getExtras().getString("OrderID"));
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }

                // nested class used for the menu adapter
                private class OrderAdapter extends ArrayAdapter<OrderItem> {

                    // constructor
                    public OrderAdapter(List<OrderItem> items) {
                        super(PayOrder.this, 0, items);
                    }

                    // function called whenever the list is created or scrolled
                    @Override
                    public View getView(int position, View view, ViewGroup parent) {
                        if (view == null) {
                            view = getLayoutInflater().inflate(R.layout.activity_pay_order_item, parent, false);
                        }

                        // get the current item
                        OrderItem item = getItem(position);

                        // get item name
                        TextView tvItemName = (TextView) view.findViewById(R.id.tvItemName);
                        tvItemName.setText(item.name);

                        // get request
                        TextView tvRequest = (TextView) view.findViewById(R.id.tvRequest);
                        tvRequest.setText(item.request);

                        // build query for price
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("MenuItem");
                        query.whereEqualTo("ItemName", item.name);

                        // run query for price
                        TextView tvPrice = (TextView) view.findViewById(R.id.tvPrice);
                        query.getFirstInBackground(new GetCallback<ParseObject>() {
                            private TextView tvPrice;

                            private GetCallback<ParseObject> initialize(TextView tvPrice) {
                                this.tvPrice = tvPrice;
                                return this;
                            }

                            @Override
                            public void done(ParseObject item, ParseException e) {
                                float price = item.getNumber("Price").floatValue();
                                String formattedPrice = String.format("$%.2f", price);
                                tvPrice.setText(formattedPrice);
                            }
                        }.initialize(tvPrice));

                        // return the view
                        return view;
                    }
                }

            }
