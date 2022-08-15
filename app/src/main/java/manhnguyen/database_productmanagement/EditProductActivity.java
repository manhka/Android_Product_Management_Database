package manhnguyen.database_productmanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class EditProductActivity extends AppCompatActivity {
    private Button btnEdit, btnCancel;
    private EditText name, price;
    Product product;
    String urlUpdate = "http://192.168.1.10/androidwebservice/product/updatedata.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        Mapping();
        product = (Product) getIntent().getExtras().get("object edit");
        name.setText(product.getName());
        price.setText(String.valueOf(product.getPrice()));
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProductActivity.this, MainActivity.class));
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateProduct(product);
            }
        });
    }

    private void UpdateProduct(Product product) {
        String newName = name.getText().toString().trim();
        Long newPrice = Long.valueOf(price.getText().toString().trim());
        if (newName.isEmpty() || newPrice.toString().isEmpty()) {
            Toast.makeText(EditProductActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
        } else {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpdate,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("success")) {
                                Intent intent = new Intent();
                                setResult(RESULT_OK,intent);
                                finish();
                            } else {
                                Toast.makeText(EditProductActivity.this, "Edit fail!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(EditProductActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> paramsEDIT = new HashMap<>();
                    paramsEDIT.put("productId", String.valueOf(product.getId()));
                    paramsEDIT.put("productName", newName);
                    paramsEDIT.put("productPrice", String.valueOf(newPrice));
                    return paramsEDIT;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    private void Mapping() {
        btnCancel = (Button) findViewById(R.id.btnCancelEdit);
        btnEdit = (Button) findViewById(R.id.btnEditProduct);
        name = (EditText) findViewById(R.id.editTextNameEdit);
        price = (EditText) findViewById(R.id.editTextPriceEdit);
    }
}