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

public class AddProductActivity extends AppCompatActivity {
    private Button btnAdd, btnCancel;
    private EditText name, price;
    String urlInsert = "http://192.168.1.10/androidwebservice/product/insertdata.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Mapping();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewProduct();
            }
        });
    }

    private void CreateNewProduct() {
        String nameAdd = name.getText().toString().trim();
        Long priceAdd = Long.valueOf(price.getText().toString().trim());
        if (nameAdd.isEmpty() || priceAdd.toString().isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
        } else {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, urlInsert,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("success")) {
                                Toast.makeText(AddProductActivity.this, "Add success!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddProductActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(AddProductActivity.this, "Add fail!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AddProductActivity.this, "ERROR!", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> paramsInsert = new HashMap<>();
                    paramsInsert.put("productName", nameAdd);
                    paramsInsert.put("productPrice", String.valueOf(priceAdd));
                    return paramsInsert;
                }
            };
            requestQueue.add(stringRequest);
        }

    }

    private void Mapping() {
        btnCancel = (Button) findViewById(R.id.btnCancelAdd);
        btnAdd = (Button) findViewById(R.id.btnAddProduct);
        name = (EditText) findViewById(R.id.editTextNameAdd);
        price = (EditText) findViewById(R.id.editTextPriceAdd);
    }
}