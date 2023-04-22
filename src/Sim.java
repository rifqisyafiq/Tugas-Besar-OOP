import java.util.*;
public class Sim implements AksiAktif, AksiDitinggal, AksiPasif{
    private String namaLengkap;
    private Pekerjaan pekerjaan;
    private int uang;
    private Rumah rumah;
    private HashMap<String, Integer> inventory = new HashMap<>();
    private Barang[] onDelivery;
    private Kesejahteraan kesejahteraan;
    private String status;
    private Posisi posisi;


/* -------------------KONSTRUKTOR------------------- */

    public Sim(String namaLengkap){
    // Menginisiasi kesejahteraan dan uang
    Kesejahteraan kesejahteraan = new Kesejahteraan(false,80,80,80);
    this.kesejahteraan = kesejahteraan;
    this.uang = 100;

    // Menginisiasi pekerjaan secara random
    Pekerjaan[] daftarPekerjaan = {
        new Pekerjaan("Badut Sulap", 15),
        new Pekerjaan("Koki", 30),
        new Pekerjaan("Polisi", 35),
        new Pekerjaan("Programmer", 45),
        new Pekerjaan("Dokter", 50),
    };
    Random random = new Random();
    int randomIndex = random.nextInt(daftarPekerjaan.length);
    changePekerjaan(daftarPekerjaan[randomIndex]);

    // Menginisiasi rumah
    Rumah rumah = new Rumah(new Point(0,0));
    this.rumah=rumah;
    this.posisi= new Posisi(rumah, rumah.getArrayOfRuangan().get(0), null);

    //menginisiasi nama
    this.namaLengkap = namaLengkap;
    }


/* ----------------------GETTER-------------------------- */

    public String getName(){
        return namaLengkap;
    }

    public Pekerjaan getPekerjaan(){
        return pekerjaan;
    }

    public int getUang(){
        return uang;
    }

    public Rumah getRumah(){
        return rumah;
    }

    public HashMap<String, Integer> getInventory(){
        return inventory;
    }

    public Barang[] getOnDelivery(){
        return onDelivery;
    }

    public Kesejahteraan getKesejahteraan(){
        return kesejahteraan;
    }

    public String getStatus(){
        return status;
    }

    public Posisi getPosisi(){
        return posisi;
    }

/* --------------------------SETTER------------------------------ */

    public void setName(String namaLengkap){
        this.namaLengkap = namaLengkap;
    }

    public void setPekerjaan(Pekerjaan pekerjaan){
        this.pekerjaan = pekerjaan;
    }

    public void setUang(int uang){
        this.uang = uang;
    }

    public void setRumah(Rumah rumah){
        this.rumah = rumah;
    }

    public void setInventory(HashMap<String, Integer> inventory){
        this.inventory = inventory;
    }

    public void setOnDelivery(Barang[] onDelivery){
        this.onDelivery = onDelivery;
    }

    public void setKesejahteraan(Kesejahteraan kesejahteraan){
        this.kesejahteraan = kesejahteraan;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public void setPosisi(Posisi posisi){
        this.posisi = posisi;
    }

    public void changePekerjaan(Pekerjaan kerjaBaru){
        this.pekerjaan = kerjaBaru;
    }
    
/* ----------------------GO TO OBJECT---------------------------- */
   
    public void gotToBarang(Barang barang){

    }

/* -------------------------------- AKSI DITINGGAL ----------------------------------- */

    @Override
    public void bunuhDiri(){
        kesejahteraan.setMood(0);
        kesejahteraan.setKesehatan(0);
        kesejahteraan.setKekenyangan(0);
        kesejahteraan.setDead(true);
    }

    public void upgradeRumah(String ruanganBaru, String posisi){}
    public void beliBarang(String namaBarang){}


/* ------------------------------- AKSI PASIF ----------------------------------- */
    public void moveToRoom(String ruangTujuan){}

    public void lihatInventory(){}

    public void pasangBarang(String namaBarang, int x, int y){
        NonMakanan barang = new NonMakanan(namaBarang);
        barang.setPosisi(new Point(x, y));
    }

    public void lihatWaktu(){
        
    }

    public void pukulSim(Sim otherSim){
        int kesehatanAwal = otherSim.getKesejahteraan().getKesehatan();
        otherSim.getKesejahteraan().setKesehatan(kesehatanAwal-10);
    }

    public void bercanda(Sim otherSim){
        int moodAwal = otherSim.getKesejahteraan().getMood();
        otherSim.getKesejahteraan().setMood(moodAwal+10);
    }

/* --------------------------------AKSI AKTIF ------------------------------------ */

    public void kerja(int waktu){}

    public void olahraga(int waktu){}

    public void makan(Makanan makanan){}

    public void makan(BahanMakanan bahanmakanan){}

    public void tidur(int waktu){}

    public void masak(Makanan makanan){}

    public void berkunjung(Rumah rumahSim){}

    public void buangAir(){}

    public void nontonTV(int waktu){}

    public void ngoding(int waktu, String bahasaProgram){}

    public void dengerMusik(int waktu, String genre){}

    public void mainGame(int waktu){}

}