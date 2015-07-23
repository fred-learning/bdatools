package bda.test

/**
 * Created by Administrator on 2015/5/25.
 */
object CaseTables {
  //table 1:store_sales
  //table 2:store_returns
  case class store_returns(sr_returned_date_sk:String,sr_return_time_sk:String,sr_item_sk:String,sr_customer_sk:String,sr_cdemo_sk:String,
                           sr_hdemo_sk:String,sr_addr_sk:String,sr_store_sk:String,sr_reason_sk:String,sr_ticket_number:String,
                           sr_return_quantity:Int,sr_return_amt:Float,sr_return_tax:Float,sr_return_amt_inc_tax:Float,sr_fee:Float,
                           sr_return_ship_cost:Float,sr_refunded_cash:Float,sr_reversed_charge:Float,sr_store_credit:Float,sr_net_loss:Float)
  //table 3:catalog_sales
  //table 4:catalog_returns
  //table 5:web_sales
  //table 6:web_returns
  //table 7:inventory
  case class inventory(inv_date_sk:String,inv_item_sk:String,inv_warehouse_sk:String,inv_quantity_on_hand:Int)
  //table 8:store
  //table 9:call_center
  //table 10:catalog_page
  case class catalog_page(cp_catalog_page_sk:String,cp_catalog_page_id:String,cp_start_date_sk:Int,cp_end_date_sk:Int,cp_department:String,
                          cp_catalog_number:Int,cp_catalog_page_number:Int,cp_description:String,cp_type:String)
  //table 11:web_site
  //table 12:web_page
  case class web_page(wp_web_page_sk:String,wp_web_page_id:String,wp_rec_start_date:String,wp_rec_end_date:String,wp_creation_date_sk:String,
                      wp_access_date_sk:String,wp_autogen_flag:String,wp_customer_sk:String,wp_url:String,wp_type:String,wp_char_count:Int,
                      wp_link_count:Int,wp_image_count:Int,wp_max_ad_count:Int)
  //table 13:warehouse
  case class warehouse(w_warehouse_sk:String,w_warehouse_id:String,w_warehouse_name:String,w_warehouse_sq_ft:Int,w_street_number:String,
                       w_street_name:String,w_street_type:String,w_suite_number:String,w_city:String,w_county:String,w_state:String,
                       w_zip:String,w_country:String,w_gmt_offset:Float)
  //table 14:customer
  case class customer(c_customer_sk:String,c_customer_id:String,c_current_cdemo_sk:String,c_current_hdemo_sk:String,c_current_addr_sk:String,
                      c_first_shipto_date_sk:String,c_first_sales_date_sk:String,c_salutation:String,c_first_name:String,c_last_name:String,
                      c_preferred_cust_flag:String,c_birth_day:Int,c_birth_month:Int,c_birth_year:Int,c_birth_country:String,
                      c_login:String,c_email_address:String,c_last_review_date_sk:String)
  //table 15:customer_address
  case class customer_address(ca_address_sk:String,ca_address_id:String,ca_street_number:String,ca_street_name:String,ca_street_type:String,
                              ca_suite_number:String,ca_city:String,ca_county:String,ca_state:String,ca_zip:String,ca_country:String,
                              ca_gmt_offset:Float,ca_location_type:String)
  //table 16:customer_demographics
  case class customer_demographics(cd_demo_sk:String,cd_gender:String,cd_marital_status:String,cd_education_status:String,
                                   cd_purchase_estimate:Int,cd_credit_rating:String,cd_dep_count:Int,cd_dep_employed_count:Int,
                                   cd_dep_college_count:Int)
  //table 17:date_dim
  //table 18:household_demographics
  case class household_demographics(hd_demo_sk:String,hd_income_band_sk:String,hd_buy_potential:String,hd_dep_count:Int,hd_vehicle_count:Int)
  //table 19:item
  case class item(i_item_sk:String,i_item_id:String,i_rec_start_date:String,i_rec_end_date:String,i_item_desc:String,i_current_price:Float,
                  i_wholesale_cost:Float,i_brand_id:Int,i_brand:String,i_class_id:Int,i_class:String,i_category_id:Int,
                  i_category:String,i_manufact_id:Int,i_manufact:String,i_size:String,i_formulation:String,i_color:String,
                  i_units:String,i_container:String,i_manager_id:Int,i_product_name:String)
  //table 20:income_band
  case class income_band(ib_income_band_sk:String,ib_lower_bound:Int,ib_upper_bound:Int)
  //table 21:promotion
  case class promotion(p_promo_sk:String,p_promo_id:String,p_start_date_sk:String,p_end_date_sk:String,p_item_sk:String,p_cost:Float,
                       p_response_target:Int,p_promo_name:String,p_channel_dmail:String,p_channel_email:String,p_channel_catalog:String,
                       p_channel_tv:String,p_channel_radio:String,p_channel_press:String,p_channel_event:String,p_channel_demo:String,
                       p_channel_details:String,p_purpose:String,p_discount_active:String)

  //table 22:reason
  case class reason(r_reason_sk:String,r_reason_id:String,r_reason_desc:String)

  //table 23:ship_mode
  case class ship_mode(sm_ship_mode_sk:String,sm_ship_mode_id:String,sm_type:String,sm_code:String,sm_carrier:String,sm_contract:String)
  //table 24:time_dim
  case class time_dim(t_time_sk:String,t_time_id:String,t_time:Int,t_hour:Int,t_minute:Int,t_second:Int,t_am_pm:String,
                      t_shift:String,t_sub_shift:String,t_meal_time:String)
  //table 25:dbgen_version
  case class dbgen_version(dv_version:String,dv_create_date:String,dv_create_time:String,dv_cmdline_args:String)
}
