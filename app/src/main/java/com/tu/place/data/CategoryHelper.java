/* Copyright 2016 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * For additional information, contact:
 * Environmental Systems Research Institute, Inc.
 * Attn: Contracts Dept
 * 380 New York Street
 * Redlands, California, USA 92373
 *
 * email: contracts@esri.com
 *
 */
package com.tu.place.data;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

import com.tu.place.R;
import com.tu.place.model.Place;

import java.util.Arrays;
import java.util.List;


public class CategoryHelper {

  public static String getPlaceType(String[] _type, String name){
    String type = "none";
    for(int i=0; i<_type.length; i++) if(_type[i].contains("store")) _type[i] = "store";
    List<String> listTypes = Arrays.asList(_type);
    if((name.toLowerCase().contains("hotel")|| name.toLowerCase().contains("khách sạn")))
      type = "Hotel";
    else if(listTypes.contains("food"))
      type = "Food";
    else if(listTypes.contains("atm"))
      type = "ATM";
    else if(listTypes.contains("bank"))
      type = "Bank";
    else if(listTypes.contains("bus_station"))
      type = "Bus stop";
    else if(listTypes.contains("shopping_mall")||listTypes.contains("store"))
      type = "Shop";
    else if(listTypes.contains("cafe"))
      type = "Drink";
    else if(listTypes.contains("grocery_or_supermarket"))
      type = "Super Market";
    else if(listTypes.contains("health")||listTypes.contains("hospital")||listTypes.contains("dentist")||listTypes.contains("doctor"))
      type = "Health";

    return type;
  }

  public static Integer getResourceIdForPlacePin(final Place p){
    final String category = (p.getContent());
    final Integer d;
    switch (category){
      case "Pizza":
        d = R.drawable.pizza_pin;
        break;
      case "Hotel":
        d = R.drawable.hotel_pin;
        break;
      case "Food":
        d = R.drawable.restaurant_pin;
        break;
      case "Bar or Pub":
        d = R.drawable.bar_pin;
        break;
      case "Coffee Shop":
        d = R.drawable.cafe_pin;
        break;
      default:
        d = R.drawable.empty_pin;
    }
    return d;
  }

  public static Integer getPinForCenterPlace(final Place p){
    final String category = (p.getContent());
    final Integer d;
    switch (category){
      case "Pizza":
        d = R.drawable.blue_pizza_pin;
        break;
      case "Hotel":
        d = R.drawable.blue_hotel_pin;
        break;
      case "Food":
        d = R.drawable.blue_rest_pin;
        break;
      case "Bar or Pub":
        d = R.drawable.blue_bar_pin;
        break;
      case "Coffee Shop":
        d = R.drawable.blue_cafe_pin;
        break;
      default:
        d = R.drawable.blue_empty_pin;
    }
    return d;
  }

  /**
   * Return appropriate drawable base on place type
   * @param p - Place item
   * @param a - Activity
   * @return - Drawable
   */
  public static Drawable getDrawableForPlace(final Place p, final Activity a){

    final String placeType = p.getContent();
    final String category = placeType;
    final Drawable d;
    switch (category){
      case "Shopping":
        d = ResourcesCompat.getDrawable(a.getResources(), R.drawable.ic_local_pizza_black_24dp,null);
        break;
      case "Hotel":
        d = ResourcesCompat.getDrawable(a.getResources(), R.drawable.ic_hotel_black_24dp,null);
        break;
      case "Food":
        d = ResourcesCompat.getDrawable(a.getResources(), R.drawable.ic_local_dining_black_24dp,null);
        break;
      case "Bar or Pub":
        d = ResourcesCompat.getDrawable(a.getResources(), R.drawable.ic_local_bar_black_24dp,null);
        break;
      case "Coffee Shop":
        d = ResourcesCompat.getDrawable(a.getResources(), R.drawable.ic_local_cafe_black_24dp,null);
        break;
      default:
        d = ResourcesCompat.getDrawable(a.getResources(), R.drawable.ic_place_black_24dp,null);
    }
    return d;
  }
}
