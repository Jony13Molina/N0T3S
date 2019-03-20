package com.example.jonny.n0t3s;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    public String[] inputInfo;
    public String[] showInfo;
    public ArrayList<String> writeData;
    private Context context;
    public static int atAdapter;

    public RecyclerAdapter(Context context) {
        this.context = context;
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemInfo;

        public ViewHolder( View itemView ) {
            super(itemView);
            itemInfo =
                    (TextView) itemView.findViewById(R.id.item_info);

            //atAdapter = getAdapterPosition();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    atAdapter = getAdapterPosition();

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


                    alertDialogBuilder.setTitle("Do you want to delete this item?....");
                    alertDialogBuilder.setPositiveButton("Delete",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int which) {

                                    deleteItem(atAdapter);
                                    //writeData.remove(String.valueOf(atAdapter));


                                    //notifyItemRemoved(atAdapter);
                                    //notifyItemRangeChanged(atAdapter, writeData.size());
                                }



                            });

                    alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1)
                        {
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });

        }



    }




    private void deleteItem(int index) {



                writeData.remove(String.valueOf(index));
                //notifyDataSetChanged();
                notifyItemRangeChanged(index, writeData.size());
                notifyItemRemoved(index);
                //notifyDataSetChanged();
                // notifyItemRangeChanged(index, writeData.size());


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        showInfo = getArray();
        writeData = new ArrayList<>();
        writeData.add(showInfo[i]);
        String enterInfo;
        for(i =0; i< writeData.size(); i++){

            //enterInfo = (Arrays.toString(new String[]{writeData.get(i)}));
            enterInfo = writeData.get(i);
            viewHolder.itemInfo.setText(enterInfo);

        }
        //enterInfo = writeData.get(i);
        //viewHolder.itemInfo.setText(enterInfo);



    }



    @Override
    public int getItemCount() {
        return getArray().length;
    }
/*------------------Methods to control file output and clicking action------------------*/
    public String[] getArray() {
        try {
            Scanner file_Array = new Scanner(getFile(context));
            List<String> input = new ArrayList<String>();
            while (file_Array.hasNextLine()) {
                input.add(file_Array.nextLine());
            }
            //create array to store file
            inputInfo = input.toArray(new String[0]);
            return inputInfo;


        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    public String getFile(Context context) throws IOException {

        StringBuilder output = new StringBuilder();

        try {
            //open file for reading
            InputStream info = context.openFileInput("file.txt");

            if (info != null) {
                //prepare file for reading
                InputStreamReader input = new InputStreamReader(info);
                BufferedReader read = new BufferedReader(input);
                String line = "";
                while ((line = read.readLine()) != null) {
                    output.append(line);
                    output.append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output.toString();
    }




}