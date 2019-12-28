package kernbeisser.Windows.ItemFilter;

import kernbeisser.DBEntitys.PriceList;
import kernbeisser.DBEntitys.Supplier;
import kernbeisser.Windows.Model;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

class ItemFilterModel implements Model{

    PriceList searchPriceListByName(String name){
        return PriceList.getAll("where name like '"+name+"'").get(0);
    }
    Collection<Supplier> getAllSuppliers(){
        return Supplier.getAll(null);
    }
}
