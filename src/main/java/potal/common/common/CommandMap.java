package potal.common.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.ibatis.type.Alias;
 
/**
 * request에 담겨있는 파라미터를 Map에 담아주는 역할을 하는 클래스
*/
public class CommandMap {
    Map<String,Object> map = new HashMap<String,Object>();
     
    public Object get(String key){
        return map.get(key);
    }
     
    public void put(String key, Object value){
        map.put(key, value);
    }
     
    public Object remove(String key){
        return map.remove(key);
    }
     
    public boolean containsKey(String key){
        return map.containsKey(key);
    }
     
    public boolean containsValue(Object value){
        return map.containsValue(value);
    }
     
    public void clear(){
        map.clear();
    }
    
    /*
     * - HashMap에 저장된 Key - Value갑슬 엔트리(키와 값을 결합)의 형태로 Set에 저장하여 반환
     *  map.put("A", 1);
     *  map.put("B", 2);
     *  map.put("C", 3);
	 *-> result : [A=1, B=2, C=3] 
	 *
     * Set set = map.entrySet();
     */
    public Set<Entry<String, Object>> entrySet(){
        return map.entrySet();
    }
     
    /*
     * HashMap에 저장된 모든 키가 저장된 Set을 반환한다.
     *  map.put("A", 1);
     *  map.put("B", 2);
     *  -> result : [A,B]
     * 
     * 
     */
    public Set<String> keySet(){
    	// keyset() 의 반환타입은 Set이다
        return map.keySet();
    }
     
    public boolean isEmpty(){
        return map.isEmpty();
    }
     
    public void putAll(Map<? extends String, ?extends Object> m){
        map.putAll(m);
    }
     
    public Map<String,Object> getMap(){
        return map;
    }

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
}
