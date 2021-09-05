# Bill-Book
Android lab mini project

#Schema of one customer

user{

  uname:  
  contact:
  email:
  shop_name:
  item_name{
            id:name
  }
  item_price{
            id:price
  }
  item_code{
            id:code
  }
  sales{
        id{
          customer_name:name
          customer_email:email
          date:date
          total:total
          items:ListOfItems
        }
  }

}



