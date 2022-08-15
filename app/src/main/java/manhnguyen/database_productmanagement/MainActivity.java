package manhnguyen.database_productmanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    EditText inputSearch;
    CircleImageView search;
    TextView title;
    CircleImageView add;
    RecyclerView viewListProduct;
    ProductAdapter adapter;
    String urlGet = "http://192.168.1.10/androidwebservice/product/getdata.php";
    String urlDelete = "http://192.168.1.10/androidwebservice/product/deletedata.php";
    List<Product> productList;
    private int REQUEST_CODE_EDIT = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Mapping();
        adapter = new ProductAdapter(new ProductAdapter.ClickItem() {
            @Override
            public void DeleteProduct(Product product) {
                ClickDelete(product);
            }

            @Override
            public void UpdateProduct(Product product) {
                ClickUpdate(product);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        viewListProduct.setLayoutManager(linearLayoutManager);
        viewListProduct.setAdapter(adapter);
        GetListProduct(urlGet);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchByName();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProduct();
            }
        });

    }

    private void AddProduct() {
        Intent intent = new Intent(MainActivity.this, AddProductActivity.class);
        startActivity(intent);
    }


    private void ClickUpdate(Product product) {
        Intent intent = new Intent(MainActivity.this, EditProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object edit", product);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    private void ClickDelete(Product product) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlDelete,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.toString().trim().equals("success")) {
                            GetListProduct(urlGet);
                            Toast.makeText(MainActivity.this, "Delete Success!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Delete Fail!", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "ERROR!", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("productId", String.valueOf(product.getId()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void GetListProduct(String url) {
        productList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                productList.add(new Product(jsonObject.getInt("ID"),
                                        jsonObject.getString("NAME"),
                                        jsonObject.getLong("PRICE")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.GetData(productList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    private void Mapping() {
        inputSearch = (EditText) findViewById(R.id.editTextInputSearch);
        search = (CircleImageView) findViewById(R.id.circleImageSearch);
        title = (TextView) findViewById(R.id.TextViewTitle);
        add = (CircleImageView) findViewById(R.id.circleImageAdd);
        viewListProduct = (RecyclerView) findViewById(R.id.recycleViewList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK && data != null) {
            GetListProduct(urlGet);
        } else {
            Toast.makeText(this, "CODE ERROR!", Toast.LENGTH_SHORT).show();
        }
    }

    private void SearchByName() {
        String searchName = inputSearch.getText().toString();
        List<Product> productListSearch = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getName().toLowerCase().contains(searchName.toLowerCase())) {
                productListSearch.add(new Product(productList.get(i).getId(), productList.get(i).getName(), productList.get(i).getPrice()));
                adapter.GetData(productListSearch);
            } else if (i == productList.size()) {
                Toast.makeText(this, "Product: '" + searchName + "' does not exist ?", Toast.LENGTH_SHORT).show();
            }
        }

    }
}