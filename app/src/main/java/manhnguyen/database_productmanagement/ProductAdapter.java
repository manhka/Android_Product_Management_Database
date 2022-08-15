package manhnguyen.database_productmanagement;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<Product> products;
    ClickItem clickItem;

    public ProductAdapter(ClickItem clickItem) {
        this.clickItem = clickItem;
    }

    public void GetData(List<Product> list) {
        this.products = list;
        notifyDataSetChanged();
    }

    public interface ClickItem {
        void DeleteProduct(Product product);

        void UpdateProduct(Product product);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        if (product != null) {
            holder.name.setText(product.getName());
            holder.price.setText(String.valueOf(product.getPrice()));
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickItem.UpdateProduct(product);
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(view.getContext());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_delete_product);
                    dialog.setCanceledOnTouchOutside(false);
                    Button btnDelete = dialog.findViewById(R.id.btnDelete);
                    Button btnCancel = dialog.findViewById(R.id.btnCancel);
                    TextView announceDelete = dialog.findViewById(R.id.deleteAnnounce);
                    announceDelete.setText("Do you want to remove product: '" + product.getName() + "' ?");
                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clickItem.DeleteProduct(product);
                            Toast.makeText(view.getContext(), "Delete", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }

            });
        }
    }

    @Override
    public int getItemCount() {
        if (products == null) {
            return 0;
        }
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price;
        CircleImageView edit, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.textViewName);
            price = (TextView) itemView.findViewById(R.id.textViewPrice);
            edit = (CircleImageView) itemView.findViewById(R.id.circleImageEdit);
            delete = (CircleImageView) itemView.findViewById(R.id.circleImageDelete);
        }
    }
}
