package simplicity;
import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;


public class JSONreader {

    //method untuk melakukan set ulang world sesuai kondisi di dalam file load
    public void readWorld(World world, String namafile) {
        // JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonobj = new JSONObject();
        try (
            FileReader reader = new FileReader(namafile)) {
            // Read JSON file
            Object obj = jsonParser.parse(reader);
            jsonobj = (JSONObject) obj;
            ArrayList<Object> daftarPosisi = new ArrayList<>();
            //mengatur atribut world
            String hari = jsonobj.get("Hari").toString();
            world.setHari(Integer.parseInt(hari));
            String waktu = jsonobj.get("waktu").toString();
            world.setWaktu(Integer.parseInt(waktu));
            JSONArray arrsim = (JSONArray) jsonobj.get("ArrSim");
            for (Object i : arrsim) {
                world.getArrSim().add(readSim((JSONObject) i,daftarPosisi));
            }
            //mengatur pemosisian ruangan di dalam rumah
            for (Sim i: world.getArrSim()){
                String rumah="";
                String ruangan="";
                for (Object m: daftarPosisi){
                    JSONObject n = (JSONObject)m;
                    if (i.getName().equals(n.get("nama").toString())){
                        JSONObject posisi = (JSONObject)n.get("posisi");
                        rumah = posisi.get("currRumah").toString();
                        ruangan = posisi.get("currRuangan").toString();
                    }
                }
                for (Sim j:world.getArrSim()){
                    if (j.getName().equals(rumah)){
                        i.getPosisi().setCurrRumah(j.getRumah());
                        for (Ruangan k: i.getPosisi().getCurrRumah().getArrayOfRuangan()){
                            if (k.getNamaRuangan().equals(ruangan)){
                                i.getPosisi().setCurrRuangan(k);
                            }
                        }
                    }
                }
        }
        //mengatur ulang ruangan yang sedang dibangun dengan menjalankan ulang thread
        for (Sim i : world.getArrSim()){
            for (Ruangan j : i.getRumah().getArrayOfRuangan()){
                Ruangan ruangan = j;
                for (Ruangan k: i.getRumah().getArrayOfRuangan()){
                    if (ruangan.getNamaRuangan()!=null && k.getRuangAtas()!=null && ruangan.getNamaRuangan().equals(k.getRuangAtas().getNamaRuangan())){
                        k.setRuangAtas(ruangan);
                        ruangan.setRuangBawah(k);
                    }
                    if (ruangan.getNamaRuangan()!=null && k.getRuangBawah()!=null && ruangan.getNamaRuangan().equals(k.getRuangBawah().getNamaRuangan())){
                        k.setRuangBawah(ruangan);
                        ruangan.setRuangAtas(k);
                    }
                    if (ruangan.getNamaRuangan()!=null && k.getRuangKanan()!=null && ruangan.getNamaRuangan().equals(k.getRuangKanan().getNamaRuangan())){
                        k.setRuangKanan(ruangan);
                        ruangan.setRuangKiri(k);
                    }
                    if (ruangan.getNamaRuangan()!=null && k.getRuangKiri()!=null && ruangan.getNamaRuangan().equals(k.getRuangKiri().getNamaRuangan())){
                        k.setRuangKiri(ruangan);
                        ruangan.setRuangKanan(k);
                    }
                }
            }
            for (Ruangan j : i.getRumah().getRuanganBlomJadi()){
                Ruangan ruangan = j;
                
                for (Ruangan k: i.getRumah().getArrayOfRuangan()){
                    if (ruangan.getNamaRuangan()!=null && k.getRuangAtas()!=null && ruangan.getNamaRuangan().equals(k.getRuangAtas().getNamaRuangan())){
                        TimerRumah timerRumah = new TimerRumah(i, j.getNamaRuangan(), k, "bawah", j.getWaktuSelesai());
                        timerRumah.start();
                    }
                    if (ruangan.getNamaRuangan()!=null && k.getRuangBawah()!=null && ruangan.getNamaRuangan().equals(k.getRuangBawah().getNamaRuangan())){
                        TimerRumah timerRumah = new TimerRumah(i, j.getNamaRuangan(), k, "atas", j.getWaktuSelesai());
                        timerRumah.start();
                    }
                    if (ruangan.getNamaRuangan()!=null && k.getRuangKanan()!=null && ruangan.getNamaRuangan().equals(k.getRuangKanan().getNamaRuangan())){
                        TimerRumah timerRumah = new TimerRumah(i, j.getNamaRuangan(), k, "kiri", j.getWaktuSelesai());
                        timerRumah.start();
                    }
                    if (ruangan.getNamaRuangan()!=null && k.getRuangKiri()!=null && ruangan.getNamaRuangan().equals(k.getRuangKiri().getNamaRuangan())){
                        TimerRumah timerRumah = new TimerRumah(i, j.getNamaRuangan(), k, "kanan", j.getWaktuSelesai());
                        timerRumah.start();
                    }
                }
            }
        }
        } catch (FileNotFoundException e) {
            System.out.println("File tidak ditemukan");
            // System.exit(0);
            // throw new FileNotFoundException();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e){
            
        }
        
            
    }

    //fungsi untuk membaca objek sim
    public Sim readSim(JSONObject jsonobj,ArrayList<Object> daftarPosisi) {
        Sim sim1 = new Sim(jsonobj.get("nama lengkap").toString());
        sim1.setInventory(readInventory((JSONObject)jsonobj.get("inventory")));
        sim1.setKesejahteraan(readKesejahteraan((JSONObject)jsonobj.get("kesejahteraan")));
        sim1.setOnDelivery(readOnDelivery((JSONArray)jsonobj.get("on delivery")));
        sim1.setPekerjaan(readPekerjaan((JSONObject)jsonobj.get("pekerjaan")));
        JSONObject posisi = new JSONObject();
        posisi.put("nama",jsonobj.get("nama lengkap").toString());
        posisi.put("posisi",jsonobj.get("posisi"));
        daftarPosisi.add((Object) posisi);
        // sim1.setPosisi(new Posisi(jsonobj.get("posisi"),null,null));
        sim1.setRumah(readRumah((JSONObject) jsonobj.get("rumah")));
        sim1.setStatus((String)jsonobj.get("status"));
        sim1.setUang(Integer.parseInt(jsonobj.get("uang").toString()));
        sim1.setWaktuMakanAwal(Integer.parseInt(jsonobj.get("waktu makan awal").toString()));
        sim1.setWaktuTidurAwal(Integer.parseInt(jsonobj.get("waktu tidur awal").toString()));
        sim1.setWaktuKerja(Integer.parseInt(jsonobj.get("waktu kerja").toString()));
        sim1.setHariResign(Integer.parseInt(jsonobj.get("hari resign").toString()));
        sim1.setSudahBuangAir((boolean)jsonobj.get("sudah buang air"));
        sim1.setMakanPertama((boolean)jsonobj.get("makan pertama"));
        return sim1;
    }

    //fungsi untuk membaca objek pekerjaan
    public Pekerjaan readPekerjaan(JSONObject jsonobj){
         Pekerjaan pekerjaan1 = new Pekerjaan(jsonobj.get("nama").toString(),Integer.parseInt(jsonobj.get("gaji harian").toString()));
         return pekerjaan1;
    }

    //fungsi untuk membaca objek rumah
    public Rumah readRumah(JSONObject jsonobj) {
        Rumah rumah = new Rumah(readPoint((JSONObject) jsonobj.get("lokasi")));
        JSONArray array = (JSONArray) jsonobj.get("array of ruangan");
        JSONArray array2 = (JSONArray) jsonobj.get("ruang belum jadi");
        for (Object i : array){
            rumah.getArrayOfRuangan().add(readRuangan((JSONObject)i));
        }
        for (Object i : array2){
            rumah.getRuanganBlomJadi().add(readRuangan((JSONObject)i));
        }
        return rumah;
    }

    //fungsi untuk membaca objek point
    public Point readPoint(JSONObject jsonPoint) {
        int x = Integer.parseInt(jsonPoint.get("x").toString());
        int y = Integer.parseInt(jsonPoint.get("y").toString());
        return new Point(x, y);
    }    

    //fungsi untuk inventory dengan tipe HashMap
    public HashMap<String,Integer> readInventory(JSONObject object){
        // JSONArray inven = (JSONArray) object.get("inventory");
        HashMap<String,Integer> inventory = new HashMap<>();
        // inventory.clear();
        // Set<String> kset = inven.keySet();
        if (object.get("inventory").toString().length()>4){
            String[] spliter;
            String inventor = object.get("inventory").toString().substring(1,object.get("inventory").toString().length()-1); 
            if (inventor.contains(",")){
                String[] inven = inventor.split(",");
                for (String i: inven){
                    spliter = i.split(":");
                    inventory.put(spliter[0].substring(1, spliter[0].length()-1),Integer.parseInt(spliter[1]));
                }
            }else{
                spliter = inventor.split(":");
                inventory.put(spliter[0].substring(1, spliter[0].length()-1),Integer.parseInt(spliter[1]));
                
            }
        }
        return inventory;
    }

    //fungsi untuk membaca on delivery dengan tipe arraylist
    public ArrayList<Barang> readOnDelivery(JSONArray jsonobj) {
        ArrayList<Barang> onDelivery = new ArrayList<>();
        ArrayList<String> bahanmakanan = new ArrayList<>();
        bahanmakanan.add("Nasi");
        bahanmakanan.add("Ayam");
        bahanmakanan.add("Sapi");
        bahanmakanan.add("Wortel");
        bahanmakanan.add("Kentang");
        bahanmakanan.add("Bayam");
        bahanmakanan.add("Kacang");
        bahanmakanan.add("Susu");
        JSONArray jsonArray = jsonobj;
        for (Object i: jsonArray) {
            String nama = ((JSONObject)i).get("nama").toString();
            if (bahanmakanan.contains(nama)){
                BahanMakanan barang = new BahanMakanan(nama);
                onDelivery.add(barang);
            }else{
                NonMakanan barang = new NonMakanan(nama);
                onDelivery.add(barang);
            }   
        }
        return onDelivery;
    }
    
    //fungsi untuk membaca objek kesejahteraan
    public Kesejahteraan readKesejahteraan(JSONObject jsonobj) {
        boolean dead = (boolean) jsonobj.get("dead");
        int kesehatan =  Integer.parseInt(jsonobj.get("kesehatan").toString());
        int kekenyangan =  Integer.parseInt(jsonobj.get("kekenyangan").toString());
        int mood =  Integer.parseInt(jsonobj.get("mood").toString());
        return new Kesejahteraan(dead, mood, kesehatan, kekenyangan);
    }
    
    //fungsi untuk membaca objek posisi
    public Object readPosisi(Sim sim,JSONObject jsonobj) {
        JSONObject posisi = new JSONObject();
        posisi.put("nama",sim.getName());
        posisi.put("posisi",jsonobj);
        // posisi.put("")
        return (Object) posisi;
    }
    
    //fungsi untuk membaca objek ruangan
    public Ruangan readRuangan(JSONObject obj) {
        String namaRuangan = obj.get("nama").toString();
        if (namaRuangan.equals("null")) {
            return null;
        }
        Ruangan ruangan = new Ruangan(namaRuangan);
        if (obj.get("ruang atas")!=null){
                Ruangan ruangAtas = new Ruangan(obj.get("ruang atas").toString());
                ruangan.setRuangAtas(ruangAtas);
        }
        
        if (obj.get("ruang bawah")!=null){
                Ruangan ruangBawah = new Ruangan(obj.get("ruang bawah").toString());
                ruangan.setRuangBawah(ruangBawah);
            
        }
        if (obj.get("ruang kanan")!=null){
                Ruangan ruangKanan = new Ruangan(obj.get("ruang kanan").toString());
                ruangan.setRuangKanan(ruangKanan);
        
        }
        if (obj.get("ruang kiri")!=null){
                Ruangan ruangKiri = new Ruangan(obj.get("ruang kiri").toString());
                ruangan.setRuangKiri(ruangKiri);
        }
    
        JSONArray arrayOfBarang = (JSONArray) obj.get("array of barang");
        ruangan.getBarangInRuangan().clear();
        if (arrayOfBarang != null) {
            for (Object i : arrayOfBarang) {
                JSONObject barangObj = (JSONObject) i;
                if (barangObj != null) {
                    Barang barang = readBarang(barangObj);
                    if (barang != null) {
                        ruangan.addBarangInRuangan(barang);
                }
            }
            }
            
        }
        ruangan.setWaktuSelesai(Integer.parseInt(obj.get("waktu selesai").toString()));
    
        return ruangan;
    }

    //fungsi untuk membaca objek NonMakanan
    public NonMakanan readNonMakanan(JSONObject obj) {
        String nama = obj.get("nama").toString();
        if (!nama.equals("null")){
            NonMakanan nonMakanan = new NonMakanan(nama);
            nonMakanan.setOrientasi(Integer.parseInt(obj.get("orientasi").toString()));
            nonMakanan.setShippingTime(Integer.parseInt(obj.get("shipping time").toString()));
            nonMakanan.setPosisi(readPoint((JSONObject) obj.get("posisi")));
            nonMakanan.setWaktuSelesai(Integer.parseInt(obj.get("waktu selesai").toString()));
            return nonMakanan;
        }else{
            return null;
        }
    }
    
    //fungsi untuk membaca objek barang
    public NonMakanan readBarang(JSONObject obj) {
        String nama = obj.get("nama").toString();
        if (!nama.equals("null")){
            NonMakanan nonMakanan = new NonMakanan(nama);
            nonMakanan.setOrientasi(Integer.parseInt(obj.get("orientasi").toString()));
            nonMakanan.setShippingTime(Integer.parseInt(obj.get("shipping time").toString()));
            nonMakanan.setPosisi(readPoint((JSONObject) obj.get("posisi")));
            nonMakanan.setWaktuSelesai(Integer.parseInt(obj.get("waktu selesai").toString()));
            return nonMakanan;
        }else{
            return null;
        }
    }
}
