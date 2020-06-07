package com.example.vfast.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vfast.MainActivity;
import com.example.vfast.Model.ModelOrders;
import com.example.vfast.OrderDetails;
import com.example.vfast.R;
import com.example.vfast.customerPages.CustomerMain;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterOrders  extends RecyclerView.Adapter<AdapterOrders.MyHolder>{

    boolean isUserPanel=false;
    Context context;
    List<ModelOrders> orderlist;
    public AdapterOrders(Context context, List<ModelOrders> orderlist,boolean isUserPanel) {
        this.context = context;
        this.orderlist = orderlist;
        this.isUserPanel=isUserPanel;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_delevery_order,parent,false);
        return new AdapterOrders.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int i) {


        holder.pickup_address_id.setText(orderlist.get(i).getPickUp_Address());
        holder.endup_address_id.setText(orderlist.get(i).getEndUp_Address());
        holder.pricetxt.setText("Price:"+orderlist.get(i).getProductPrice()+"$");
        holder.weighttxt.setText("Weight : "+orderlist.get(i).getProductWeight());
        holder.producttxt.setText("Product : "+orderlist.get(i).getProduct());
        holder.Unametv.setText(orderlist.get(i).getUser_Name());
       // Toast.makeText(context, "Delete From Firebase "+orderlist.get(i).getProductKey(), Toast.LENGTH_SHORT).show();

        //setting status
        String status=orderlist.get(i).getStatus();
        if (status.equals("-1")){
            holder.statustxt.setText("Status : Pending");
        }
        else if(status.equals("0")){
            holder.statustxt.setText("Status : Searching");
        }
        else {
            holder.statustxt.setText("Status : OnWay");
        }

        final String currentProductKey=orderlist.get(i).getProductKey();

        if (isUserPanel){
            //setting status
            //here if price isn't zero that mean admin set some kind of price and user will accept it over denie it
            String price=orderlist.get(i).getProductPrice();
            if (!price.equals("0")&&status.equals("-1")){
                holder.linear1.setVisibility(View.VISIBLE);
                holder.adminDeciTxt.setVisibility(View.GONE);
                holder.pricetxt2.setText("Price :"+orderlist.get(i).getProductPrice());

                //setting listner
                holder.cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //delete from firebase
                        AlertDialog.Builder builder = new AlertDialog.Builder(context,4);
                        builder.setMessage("Are you sure You Want To Cancel This Order?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //delete now
                                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Order");
                                ref.child(currentProductKey).removeValue(); //
                                //notifyItemRemoved(i);

                                //orderlist.remove(i);
                                //  context.startActivity(context, MainActivity.);

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                    }
                });

                holder.acceptOrderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Order");
                        ref.child(orderlist.get(i).getProductKey()).child("Status").setValue("0");//now deleboy can see orders
                       // context.startActivity(new Intent(context, CustomerMain.class));
                    }
                });

            }
            else if(!price.equals("0")&&!status.equals("-1")){
                //admin accpeted  order
                holder.linear1.setVisibility(View.GONE);
                holder.adminDeciTxt.setVisibility(View.VISIBLE);
                holder.adminDeciTxt.setText("Order Placed");
            }
            else {
                holder.linear1.setVisibility(View.GONE);
                holder.adminDeciTxt.setVisibility(View.VISIBLE);
            }
            //this is deletion process
          /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete from firebase
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //delete now
                            DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Order");
                            ref.child(currentProductKey).removeValue(); //
                          //notifyItemRemoved(i);

                            //orderlist.remove(i);
                          //  context.startActivity(context, MainActivity.);

                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                                }
                            }).show();

                }
            });

           */

          holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
              @Override
              public boolean onLongClick(View view) {
                  //delete from firebase
                  AlertDialog.Builder builder = new AlertDialog.Builder(context,4);
                  builder.setMessage("Are you sure You Want To Cancel This Order?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {
                          //delete now
                          DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Order");
                          ref.child(currentProductKey).removeValue(); //
                          //notifyItemRemoved(i);

                          //orderlist.remove(i);
                          //  context.startActivity(context, MainActivity.);

                      }
                  }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialogInterface, int i) {

                          Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                      }
                  }).show();

                  return false;
              }
          });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //goto orderDetails
              //TODO  goto details page  Intent intent=new Intent(context, OrderDetails.class);
                Intent intent=new Intent(context, OrderDetails.class);
               intent.putExtra("productKey",orderlist.get(i).getProductKey());
               if (isUserPanel) {
                   intent.putExtra("isDeleBoy",false);
               }else {
                   intent.putExtra("isDeleBoy",true);
               }
               context.startActivity(intent);
               // Toast.makeText(context, ""+orderlist.get(i).getAllocated_DeleID(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        //view  from row post
        public TextView Unametv,pickup_address_id,producttxt,pricetxt,weighttxt,endup_address_id,pricetxt2,statustxt;
        public ImageView acceptOrderBtn,cancelOrderBtn;

        LinearLayout linear1;

        public TextView adminDeciTxt;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            Unametv=(TextView)itemView.findViewById(R.id.Unametv);
            pickup_address_id=(TextView)itemView.findViewById(R.id.pickup_address_id);
            producttxt=(TextView)itemView.findViewById(R.id.producttxt);
            pricetxt=(TextView)itemView.findViewById(R.id.pricetxt);
            weighttxt=(TextView)itemView.findViewById(R.id.weighttxt);
            endup_address_id=(TextView)itemView.findViewById(R.id.endup_address_id);
            adminDeciTxt=itemView.findViewById(R.id.adminDeciTxt);
            statustxt=itemView.findViewById(R.id.statustxt);
            linear1=itemView.findViewById(R.id.linear1);
            pricetxt2=itemView.findViewById(R.id.pricetxt2);
            acceptOrderBtn=itemView.findViewById(R.id.acceptOrderBtn);
            cancelOrderBtn=itemView.findViewById(R.id.cancelOrderBtn);

            if (isUserPanel){
                           //   user_deleteOrder=itemView.findViewById(R.id.user_deleteOrder);
              //  user_deleteOrder.setVisibility(View.VISIBLE);
            }

        }


    }
}
