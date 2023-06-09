package simplicity;
import java.io.FileNotFoundException;
import java.util.*;

public class MenuGame {

    //Atribut di dalam game
    private Sim currSim;
    private World world = World.getInstance();
    private JSONWriter writer = new JSONWriter();
    private JSONreader reader = new JSONreader();
    private int simSpawn = -1;
    Display display = new Display();
    Scanner scan = new Scanner(System.in);
    Random random = new Random();

    //Method utama
    public void startGame(){
        boolean masuk = false;
        boolean gameover = false;
        while (!masuk){
            System.out.println("Silakan memilih menu");
            System.out.println("1. New Game");
            System.out.println("2. Load");
            try {
                int menu = Integer.parseInt(scan.nextLine());
                if (menu==1){
                    addSim();
                    currSim = world.getArrSim().get(0);
                    masuk=true;
                }else if(menu==2){
                    try {
                        load();
                        masuk=true;
                    }catch(FileNotFoundException e){
                        System.out.println("File tidak ditemukan");
                    }
                }
                
                if (currSim==null){
                    masuk=false;
                }
            }catch(NumberFormatException e){
  
            }

        }
        //pilihan load game atau new game
        while (!gameover){
            System.out.println("Apa yang ingin anda lakukan?");
            String aksi = scan.nextLine();
            if (aksi.equals("View Sim Info")){
                viewSimInfo();
            }else if(aksi.equals("View Current Location")){
                viewCurrentLocation();
            }else if(aksi.equals("View Inventory")){
                currSim.lihatInventory();
            }else if(aksi.equals("Upgrade House")){
                upgradeHouse();
            }else if(aksi.equals("Edit Room")){
                editRoom();
            }else if(aksi.equals("Add Sim")){
                addSim();   
            }else if(aksi.equals("Change Sim")){
                changeSim();
            }else if(aksi.equals("List Object")){
                listObject();
            }else if(aksi.equals("Go To Object")){
                goToObject();    
            }else if(aksi.equals("Kerja")){
                try{
                    System.out.println("Masukkan waktu dalam satuan detik (kelipatan 120)");
                    int kerja = Integer.parseInt(scan.nextLine());
                    if (kerja%120==0){
                        currSim.kerja(kerja);
                    }else{
                        System.out.println("Masukan waktu tidak valid");
                    }
                }catch(NumberFormatException e){
                    System.out.println("Masukan tidak valid");
                }
            }else if(aksi.equals("Olahraga")){
                try{
                    System.out.println("Masukkan waktu dalam satuan detik (kelipatan 20)");
                    int olahraga = Integer.parseInt(scan.nextLine());
                    if (olahraga%20==0){
                        currSim.olahraga(olahraga);
                    }else{
                        System.out.println("Masukan waktu tidak valid");
                    }
                }catch(NumberFormatException e){
                    System.out.println("Masukan tidak valid");
                }
            }else if(aksi.equals("Berkunjung")){
                if (world.getArrSim().size()>1){
                    System.out.println("Berikut tetangga di sekitar sim");
                    int n=0;
                    int j=0;
                    int k=0;
                    while (n<world.getArrSim().size()){
                        if (!world.getArrSim().get(n).equals(currSim)){
                            System.out.println(j+1+world.getArrSim().get(n).getName());    
                            k=n;
                        }else{
                            j=n-1;
                        }
                        n++;
                        j++;
                    }
                    try {
                        System.out.println("Masukan no tetangga");
                        Integer tetangga = Integer.parseInt(scan.nextLine());
                        int i=0;
                        if (k<tetangga){
                            i-=-1;
                        }
                        while (i<world.getArrSim().size() && i<tetangga){
                            i++;
                        }
                        currSim.berkunjung(world.getArrSim().get(i).getRumah());
                    }catch(NumberFormatException e){
                        System.out.println("Masukan tidak valid");
                    }catch(IndexOutOfBoundsException e){
                        System.out.println("Gagal Berkunjung");
                    }
                }else{
                    System.out.println("Tidak dapat berkunjung");
                }
                //whilenya masih perlu diitung
            }else if(aksi.equals("Beli Barang")){
                beliBarang();
            }else if(aksi.equals("Move Room")){
               
                for (int  i=0;i<currSim.getPosisi().getCurrRumah().getArrayOfRuangan().size();i++){
                    System.out.println(i+1+". "+currSim.getPosisi().getCurrRumah().getArrayOfRuangan().get(i).getNamaRuangan());
                }
                System.out.println("Silakan masukkan angka untuk ruangan yang dipilih");
                try{
                    int noruangan = Integer.parseInt(scan.nextLine());
                    currSim.moveToRoom(currSim.getPosisi().getCurrRumah().getArrayOfRuangan().get(noruangan-1));
                }catch(NumberFormatException e){
                    System.out.println("Masukan tidak valid");
                }catch(IndexOutOfBoundsException e){
                    System.out.println("Gagal berpindah");
                }
            }else if (aksi.equals("Change Job")){
                changeJOb();
            }else if(aksi.equals("Memasang Barang")){
                currSim.getPosisi().getCurrRuangan().display();
                pasangBarang();
            }else if(aksi.equals("Help")){
                help();
            }else if(aksi.equals("Pukul")){
                pukul();
            }else if(aksi.equals("Action")){
                action();
            }else if(aksi.equals("Bercanda")){
                bercanda();
            }else if(aksi.equals("Bunuh Diri")){
                bunuhDiri();
                if (world.getArrSim().isEmpty()){
                    gameover=true;
                }
            }else if(aksi.equals("Exit")){
                System.out.println("Silakan pilih!");
                System.out.println("1. Save");
                System.out.println("2. Exit");
                try{
                    int no = Integer.parseInt(scan.nextLine());
                    if (no==1){
                        System.out.println("Masukkan nama file!");
                        String namafile = scan.nextLine();
                        String file  = "src/main/resources/"+namafile+".json";
                        writer.writeWorld(world, file);
                    }
                }catch(NumberFormatException e){
                    System.out.println("Masukan tidak valid");
                }
                world.getArrSim().clear();
                world.setHari(0);
                world.setWaktu(0);
                simSpawn=-1;
                gameover=true;
            }else{
                System.out.println("Masukkan aksi yang sesuai");
            }
            checkSim();
            if (world.getArrSim().isEmpty()){
                gameover=true;
                display.gameover();
            }
        }
    }
    
    //Method melakukan aksi memukul
    public void pukul(){
        System.out.println("Masukkan No Sim siapa yang ingin anda pukul?");
        int j=0;
        int n=0;
        //display sim yang akan dipilih untuk dipukul
        for (Sim i : world.getArrSim()){
            if (i.getPosisi().getCurrRuangan().equals(currSim.getPosisi().getCurrRuangan())){
                System.out.println(j+1+". "+i.getName());
                j++;
            }
        }
        try {
            //memukul
            Integer otherSim = Integer.parseInt(scan.nextLine());
            for (Sim i : world.getArrSim()){
                if (i.getPosisi().getCurrRuangan().equals(currSim.getPosisi().getCurrRuangan())){
                    if (n==otherSim-1){
                        currSim.pukulSim(i);
                        System.out.println(i.getName()+" terpukul hingga babak belur");
                    }
                    n++;
                }
            }
            if (otherSim>j){
                System.out.println("Gagal memukul");
            }
        }catch(NumberFormatException e){
            System.out.println("Masukan tidak valid");
        }
    }

    //method untuk changejob
    public void changeJOb(){
        Pekerjaan[] daftarPekerjaan = {
            new Pekerjaan("Badut Sulap", 15),
            new Pekerjaan("Koki", 30),
            new Pekerjaan("Polisi", 35),
            new Pekerjaan("Programmer", 45),
            new Pekerjaan("Dokter", 50),
        };

        //display daftar pekerjaan pilihan
        for (int i=0;i<daftarPekerjaan.length;i++){
            System.out.println(i+1+". "+daftarPekerjaan[i].getNamaPekerjaan());
        }

        //memilih pekerjaan
        try{
            System.out.println("Masukkan nomor pekerjaan baru");
            int no = Integer.parseInt(scan.nextLine());
            if (no<daftarPekerjaan.length){
                currSim.changePekerjaan(daftarPekerjaan[no-1]);
            }else{
                System.out.println("Gagal berganti pekerjaan");
            }
        }catch(NumberFormatException e){
            System.out.println("Masukan tidak valid");
        }
    }

    //method untuk melakukan aksi nontonTV
    public void nontonTV(){
        try{
            System.out.println("Berapa lama?");
            int waktu = Integer.parseInt(scan.nextLine());
            currSim.nontonTV(waktu);
            System.out.println("Berhasil menonton TV");
        }catch(NumberFormatException e){
            System.out.println("Masukan tidak valid");
        }
    }

    //method untuk melakukan aksi ngoding
    public void ngoding(){
        System.out.println("Pilih dari beberapa bahasa pemrograman yang dikuasai berikut");
        System.out.println("Python,C,C++,Java");    
        String bahasa = scan.nextLine();
        try{
            System.out.println("Berapa lama?");
            int waktu = Integer.parseInt(scan.nextLine());
            currSim.ngoding(waktu, bahasa);
        }catch(NumberFormatException e){
            System.out.println("Masukan tidak valid");
        }
    }

    //method untuk melakukan aksi bercanda
    public void bercanda(){
        boolean check=false;
        //looping memberikan efek bercanda kepada sim yang berada pada ruangan yang sama
        for (Sim i: world.getArrSim()){
            if (i.getPosisi().getCurrRuangan().equals(currSim.getPosisi().getCurrRuangan())){
                currSim.bercanda(i);
                check=true;
            }
        }
        if (check){
            System.out.println("Semua orang di dalam ruangan tertawa terpingkal-pingkal");
        }else{
            System.out.println("Tidak ada orang yang diajak bercanda");
        }
    }

    //method untuk melakukan aksi mendengar musik
    public void dengarMusik(){
        System.out.println("Beberapa genre lagu yang dapat diputar");
        ArrayList<String> daftarGenre = new ArrayList<>();
        daftarGenre.add("Indie");
        daftarGenre.add("Dangdut");
        daftarGenre.add("Pop");
        daftarGenre.add("K-Pop");
        daftarGenre.add("Reggae");
        daftarGenre.add("WOTA");
        //display daftar pilihan genre musik
        for (int i=0;i<daftarGenre.size();i++){
            System.out.println(i+1+". "+daftarGenre.get(i));
        }
        try{
            System.out.println("Masukkan nomor genre");
            int genre = Integer.parseInt(scan.nextLine());
            System.out.println("Berapa lama");
            if (genre>6){
                int waktu = Integer.parseInt(scan.nextLine());
                currSim.dengerMusik(waktu, daftarGenre.get(genre-1));
            }else{
                System.out.println("Masukan tidak valid");
            }
        }catch(NumberFormatException e){
            System.out.println("Masukan tidak valid");
        }
    }

    //method untuk melakukan aksi bermain game
    public void mainGame(){
        try{
            System.out.println("Berapa lama?");
            int waktu = Integer.parseInt(scan.nextLine());
            currSim.mainGame(waktu);
        }catch(NumberFormatException e){
            System.out.println("Masukan tidak valid");
        }
    }

    //method untuk melakukan aksi membeli barang
    public void beliBarang(){
        ArrayList <String> daftarBarang = new ArrayList<>();
        daftarBarang.add("Kasur King Size");
        daftarBarang.add("Kasur Single");
        daftarBarang.add("Kasur Queen Size");
        daftarBarang.add("Toilet");
        daftarBarang.add("Laptop");
        daftarBarang.add("TV");
        daftarBarang.add("Meja dan Kursi");
        daftarBarang.add("Jam");
        daftarBarang.add("Kompor Gas");
        daftarBarang.add("Kompor Listrik");
        daftarBarang.add("Nasi");
        daftarBarang.add("Kentang");
        daftarBarang.add("Ayam");
        daftarBarang.add("Sapi");
        daftarBarang.add("Wortel");
        daftarBarang.add("Bayam");
        daftarBarang.add("Kacang");
        daftarBarang.add("Susu");
        System.out.println("+---+----------------+");
        System.out.println("|No |Nama Barang     |");
        System.out.println("+---+----------------+");

        //display barang yang dapat dibeli
        for (int i=0;i<daftarBarang.size();i++){
            if (i<9){
                System.out.print("|"+(i+1)+" |");
            }else{
                System.out.print("|"+(i+1)+"|");
            }
            System.out.print(daftarBarang.get(i));
            for (int j=0;j<16-daftarBarang.get(i).length();j++){
                System.out.print(" ");
            }
            System.out.println("|");
        }
        System.out.println("+---+----------------+");
        System.out.println("Masukkan nomor barang");
        //Memasukkan nomor barang 
        try{
            int no = Integer.parseInt(scan.nextLine());
            if (no<=daftarBarang.size()){
                currSim.beliBarang(daftarBarang.get(no-1));
            }else{
                System.out.println("Pembelian gagal");
            }
        }catch(NumberFormatException e){
            System.out.println("Masukan tidak valid");
        }

    }

    //method untuk menuju objek
    public void goToObject(){
        if (!currSim.getPosisi().getCurrRuangan().getBarangInRuangan().isEmpty()){
            //display barang di dalam ruangan
            listObject();
            System.out.println("masukkan no object yang dituju");
            try{
                Integer obj = Integer.parseInt(scan.nextLine());
                for (int i=0;i<currSim.getPosisi().getCurrRuangan().getBarangInRuangan().size();i++){
                    if (i==obj-1){
                        currSim.getPosisi().setCurrBarang(currSim.getPosisi().getCurrRuangan().getBarangInRuangan().get(i));
                        System.out.println("Berhasil berpindah ke "+currSim.getPosisi().getCurrRuangan().getBarangInRuangan().get(i).getNama());
                    }
                }
                try{
                    //memberikan opsi aksi setelah berhasil berpindah ke objek tertentu dan dapat menjalankan aksi tersebut
                    if (currSim.getPosisi().getCurrBarang().getNama().equals("Kasur Single")){
                        System.out.println("Berikut aksi yang dapat dilakukan");
                        System.out.println("1. Tidur");
                        System.out.println("2. Tidak melakukan apa-apa");
                        int no = Integer.parseInt(scan.nextLine());
                        if (no==1){
                            tidur();
                        }
                    }else if (currSim.getPosisi().getCurrBarang().getNama().equals("Kasur Queen Size")){
                        System.out.println("Berikut aksi yang dapat dilakukan");
                        System.out.println("1. Tidur");
                        System.out.println("2. Tidak melakukan apa-apa");
                        int no = Integer.parseInt(scan.nextLine());
                        if (no==1){
                            tidur();
                        }
                    }else if (currSim.getPosisi().getCurrBarang().getNama().equals("Kasur King Size")){
                        System.out.println("Berikut aksi yang dapat dilakukan");
                        System.out.println("1. Tidur");
                        System.out.println("2. Tidak melakukan apa-apa");
                        int no = Integer.parseInt(scan.nextLine());
                        if (no==1){
                            tidur();
                        }
                    }else if (currSim.getPosisi().getCurrBarang().getNama().equals("Toilet")){
                        System.out.println("Berikut aksi yang dapat dilakukan");
                        System.out.println("1. Buang Air");
                        System.out.println("2. Tidak melakukan apa-apa");
                        int no = Integer.parseInt(scan.nextLine());
                        if (no==1){
                            buangAir();
                        }
                    }else if (currSim.getPosisi().getCurrBarang().getNama().equals("Kompor Gas")){
                        System.out.println("Berikut aksi yang dapat dilakukan");
                        System.out.println("1. Memasak");
                        System.out.println("2. Tidak melakukan apa-apa");
                        int no = Integer.parseInt(scan.nextLine());
                        if (no==1){
                            memasak();
                        }    
                    }else if (currSim.getPosisi().getCurrBarang().getNama().equals("Kompor Listrik")){
                        System.out.println("Berikut aksi yang dapat dilakukan");
                        System.out.println("1. Memasak");
                        System.out.println("2. Tidak melakukan apa-apa");
                        int no = Integer.parseInt(scan.nextLine());
                        if (no==1){
                            memasak();
                        } 
                    }else if (currSim.getPosisi().getCurrBarang().getNama().equals("Meja dan Kursi")){
                        System.out.println("Berikut aksi yang dapat dilakukan");
                        System.out.println("1. Makan");
                        System.out.println("2. Tidak melakukan apa-apa");
                        int no = Integer.parseInt(scan.nextLine());
                        if (no==1){
                            makan();
                        } 
                    }else if (currSim.getPosisi().getCurrBarang().getNama().equals("Jam")){
                        System.out.println("Berikut aksi yang dapat dilakukan");
                        System.out.println("1. Melihat Waktu");
                        System.out.println("2. Tidak melakukan apa-apa");
                        int no = Integer.parseInt(scan.nextLine());
                        if (no==1){
                            lihatWaktu();
                        } 
                    }else if (currSim.getPosisi().getCurrBarang().getNama().equals("TV")){
                        System.out.println("Berikut aksi yang dapat dilakukan");
                        System.out.println("1. Menonton TV");
                        System.out.println("2. Tidak melakukan apa-apa");
                        System.out.println("Masukkan angka!");
                        int no = Integer.parseInt(scan.nextLine());
                        if (no==1){
                            nontonTV();
                        } 
                    }else if (currSim.getPosisi().getCurrBarang().getNama().equals("Laptop")){
                        System.out.println("Berikut aksi yang dapat dilakukan");
                        System.out.println("1. Ngoding");
                        System.out.println("2. Dengar Musik");
                        System.out.println("3. Main Game");
                        System.out.println("4. Tidak Melakukan apa-apa");
                        System.out.println("Masukkan angka!");
                        int no = Integer.parseInt(scan.nextLine());
                        if (no==1){
                            ngoding();
                        }else if(no==2){
                            dengarMusik();
                        }else if(no==3){
                            mainGame();
                        }
                    }
                }catch(NullPointerException e){
                    System.out.println("Gagal berpindah");
                }
            }catch(NumberFormatException e){
                System.out.println("Masukan tidak valid");
            }
        }else{
            System.out.println("Ruangan kosong");
        }
    
    }

    //method untuk melakukan pemasangan barang
    public void pasangBarang(){
        System.out.printf("|%-1s | %-10s | %-8s |%n","no", "makanan", "Jumlah");
        System.out.println("|---|------------|----------|");
        ArrayList<String> daftarMakanan = new ArrayList<>();
        daftarMakanan.add("Kasur King Size");
        daftarMakanan.add("Kasur Single");
        daftarMakanan.add("Kasur Queen Size");
        daftarMakanan.add("Toilet");
        daftarMakanan.add("Laptop");
        daftarMakanan.add("TV");
        daftarMakanan.add("Meja dan Kursi");
        daftarMakanan.add("Jam");
        daftarMakanan.add("Kompor Gas");
        daftarMakanan.add("Kompor Listrik");
        int i=1;
        //display barang yang dapat dipasang
        for (Map.Entry<String, Integer> entry : currSim.getInventory().entrySet()) {
            if (daftarMakanan.contains(entry.getKey())){
                System.out.printf("| %-1d | %-10s | %-8d |%n", i,entry.getKey(),entry.getValue());
                i++;
            }    
        }
        //pemilihan nomor barang
        try{
            System.out.println("Masukkan nomor barang");
            Integer nobarang = Integer.parseInt(scan.nextLine());
            int j=1;
            String namabarang = "";
            int k=1;
        
        //Searching barang yang akan di pasang
            for (Map.Entry<String, Integer> entry : currSim.getInventory().entrySet()) {
                if (daftarMakanan.contains(entry.getKey())){
                    if  (k<nobarang){
                        k++;
                    }else if(k==nobarang){
                        namabarang=entry.getKey();
                        break;
                    }
                }
            }
            NonMakanan barang = new NonMakanan(namabarang);
            //pemilihan posisi peletakan barang
            System.out.println("Masukkan titik peletakan barang!");
            System.out.print("X : ");
            int x=Integer.parseInt(scan.nextLine());
            System.out.print("Y : ");
            int y=Integer.parseInt(scan.nextLine());
            currSim.getPosisi().getCurrRuangan().locateBarang(barang, x,y,currSim.getInventory());
        }catch(NumberFormatException e){
            System.out.println("Masukan tidak valid");
        }catch(NullPointerException e){
            System.out.println("Tidak ada barang yang dapat dipasang");
        }
    }

    //method untuk melakukan aksi bunuh diri
    public void bunuhDiri(){
        currSim.bunuhDiri();
        checkSim();
        if (!world.getArrSim().isEmpty()){
            System.out.println("Pilih Sim lain untuk tetap bermain");
            changeSim();
        }else{
            // display.gameover();
            // System.exit(0);
        }     
    }

    //method untuk menampilkan aksi di dalam game
    public void help(){
        System.out.println("Berikut adalah command-command di dalam game yang dapat digunakan");
        System.out.println("1. View Sim Info");
        System.out.println("2. View Current Location");
        System.out.println("3. Upgrade House");
        System.out.println("4. Move Room");
        System.out.println("5. Edit Room");
        System.out.println("6. Add Sim");
        System.out.println("7. List Object");
        System.out.println("8. Go To Object");
        System.out.println("9. Action");
        System.out.println("10. Kerja");
        System.out.println("11. Pukul");
        System.out.println("12. Beli Barang");
        System.out.println("13. Olahraga");
        System.out.println("14. Berkunjung");
        System.out.println("15. Memasang Barang");
        System.out.println("16. Bercanda");
        System.out.println("17. Bunuh Diri");
        System.out.println("18. Change Sim");
        System.out.println("20. Change Job");
        
    }
    
    //method untuk melakukan aksi edit room
    public void editRoom(){
        currSim.getPosisi().getCurrRuangan().display();
        try{

            System.out.println("Anda dapat melakukan hal terhadap barang di ruangan");
            System.out.println("1. memindahkan barang\n2. meletakkan barang\n3. merotasi barang");
            int noCommand = Integer.parseInt(scan.nextLine());
            if (noCommand>3){
                System.out.println("Masukan tidak valid");
            }else{
                //Menjalankan aksi memindahkan barang
                if (noCommand==1){
                    listObject();
                    try{
                        System.out.println("masukkan nomor barang");
                        int noBarang = Integer.parseInt(scan.nextLine());
                        Barang barang = (currSim.getPosisi().getCurrRuangan().getBarangInRuangan().get(noBarang-1));
                        System.out.println("Masukkan titik pemindahan barang!");
                        System.out.print("X : ");
                        int x=Integer.parseInt(scan.nextLine());
                        System.out.print("Y : ");
                        int y=Integer.parseInt(scan.nextLine());
                        currSim.getPosisi().getCurrRuangan().moveBarang((Barang)barang, x, y);   
                    }catch(IndexOutOfBoundsException e){
                        System.out.println("Masukan tidak valid");
                    }catch(NumberFormatException e){
                        System.out.println("Masukan tidak valid");
                    }
                //menjalankan aksi memasang barang ke ruangan
                }else if(noCommand==2){
                    pasangBarang();
                //menjalankan aksi merotasi barang
                }else if (noCommand==3){
                    listObject();
                    try{
                        System.out.println("masukkan nomor barang");
                        int noBarang = Integer.parseInt(scan.nextLine());
                        Barang barang = (currSim.getPosisi().getCurrRuangan().getBarangInRuangan().get(noBarang-1));
                        System.out.print("Rotasi : ");
                        int y=Integer.parseInt(scan.nextLine());
                        currSim.getPosisi().getCurrRuangan().rotate(barang, y);
                    }catch(IndexOutOfBoundsException e){
                        System.out.println("Masukan tidak valid");
                    }catch(NumberFormatException e){
                        System.out.println("Masukan tidak valid");
                    }
                }    
            }
        }catch(NumberFormatException e){
            System.out.println("Masukan tidak valid");
        }
    }

    //method untuk memberikan info terkait kesejahteraan dan uang sim dll.
    public void viewSimInfo(){
        System.out.println("Sim Info");
        System.out.println("1. Nama: "+currSim.getName());
        System.out.println("2. Pekerjaan: "+currSim.getPekerjaan().getNamaPekerjaan());
        System.out.println("3. Kesehatan: "+currSim.getKesejahteraan().getKesehatan());
        System.out.println("4. Kekenyangan: "+currSim.getKesejahteraan().getKekenyangan());
        System.out.println("5. Mood: "+currSim.getKesejahteraan().getMood());
        System.out.println("6. Uang: "+currSim.getUang());
    }

    //method untuk menjelaskan posisi sim saat ini
    public void viewCurrentLocation(){
        System.out.println("Lokasi sim saat ini : ");
        System.out.println("Sim berada pada rumah "+world.getSimOwnRumah(currSim.getPosisi().getCurrRumah()).getName()+" pada ruangan "+currSim.getPosisi().getCurrRuangan().getNamaRuangan());
    }

    //method untuk menambah sim
    public void addSim(){
        if (simSpawn<world.getHari()){
            System.out.print("Silakan masukkan nama lengkap Sim mu : ");
            Integer[][] map = new Integer[64][64];
            for (int i=0;i<64;i++){
                for (int j=0;j<64;j++){
                    map[i][j]=0;
                }
            }
            //mengisi informasi sim (nama, lokasi rumah)
            String nama = scan.nextLine();
            Sim sim1 = new Sim(nama);
            boolean valid=false;
            int x=0;
            int y=0;
            while (!valid) {
                try{
                    System.out.println("Masukkan lokasi rumah!");
                    System.out.print("X: ");
                    x= Integer.parseInt(scan.nextLine());
                    System.out.print("Y: ");
                    y= Integer.parseInt(scan.nextLine());
                    if (x>=0 && y>=0){
                        if (map[x][y]==0){
                            valid=true;
                            map[x][y]=1;
                        }else{
                            System.out.println("Tidak dapat membangun rumah di titik tersebut");
                        }
                    }else{
                        System.out.println("Masukan tidak valid");
                    }
                }catch(NumberFormatException e){
                    System.out.println("Masukan tidak valid");
                    valid=false;
                }
            }
            sim1.getRumah().setLokasi(new Point(x, y));
            world.getArrSim().add(sim1);
            simSpawn=world.getHari();
        }else{
            System.out.println("Belum dapat menspawn sim baru");
        }
    }
    
    //method untuk mengganti sim yang dimainkan user
    public void changeSim(){
        System.out.println("Masukkan no sim dari sim-sim yang ada di bawah");
        for (int i=0;i<world.getArrSim().size();i++){
            System.out.println(i+1+". "+world.getArrSim().get(i).getName());
        }
        try{
            Integer noSim = Integer.parseInt(scan.nextLine());
            for (int i=0;i<world.getArrSim().size();i++){
                if (i==noSim-1){
                    currSim=world.getArrSim().get(i);
                }    
            }
            System.out.println("Sim saat ini "+currSim.getName());
        }catch(NumberFormatException e){
            System.out.println("Masukan tidak valid");
        }
    }

    //method untuk menghapus sim dari world jika sudah mati
    public void checkSim(){
        for (int i=0;i<world.getArrSim().size();i++){
            if (world.getArrSim().get(i).getKesejahteraan().isDead()||world.getArrSim().get(i).getKesejahteraan().getKekenyangan()==0||world.getArrSim().get(i).getKesejahteraan().getKesehatan()==0||world.getArrSim().get(i).getKesejahteraan().getMood()==0){
                world.getArrSim().remove(world.getArrSim().get(i));
            }
        }
    }

    //method untuk menuliskan daftar objek di dalam ruangan
    public void listObject(){
        System.out.println("Objek yang berada di dalam ruangan");
        for (int i=0;i<currSim.getPosisi().getCurrRuangan().getBarangInRuangan().size();i++){
            System.out.println((i+1)+". "+currSim.getPosisi().getCurrRuangan().getBarangInRuangan().get(i).getNama());
        }
    }

    //method untuk melakukan aksi tidur
    public void tidur(){
        if (currSim.getPosisi().getCurrBarang().getNama().substring(0, 5).equals("Kasur")){
            System.out.println("Berapa lama? (masukkan dalam satuan detik)");
            try{
                int waktu = Integer.parseInt(scan.nextLine());
                currSim.tidur(waktu);
                // world.setWaktu(waktu);
            }catch(NumberFormatException e){
                System.out.println("Masukan tidak valid");
            }
        }else{
            System.out.println("Sim tidak berada di kasur");
        }
    }

    //method untuk melakukan aksi memasak
    public void memasak(){
        if (currSim.getPosisi().getCurrBarang().getNama().substring(0, 6).equals("Kompor")){
            ArrayList<String> daftarmakanan = new ArrayList<>();
            ArrayList<String> resep = new ArrayList<>();
            daftarmakanan.add("Nasi Ayam");
            daftarmakanan.add("Nasi Kari");
            daftarmakanan.add("Susu Kacang");
            daftarmakanan.add("Tumis Sayur");
            daftarmakanan.add("Bistik");
            resep.add("Nasi,Ayam");
            resep.add("Nasi,Kentang,Wortel,Sapi");
            resep.add("Susu,Kacang");
            resep.add("Wortel,Bayam");
            resep.add("Kentang,Sapi");

            //display makaanan yang dapat dimasak dan resepnya
            System.out.println("+---+------------+------------------------+");
            System.out.println("|No | Masakan    | Resep                  |");
            System.out.println("+---+------------+------------------------+");
            for (int i=0;i<daftarmakanan.size();i++){
                System.out.print("| "+(i+1)+" |"+daftarmakanan.get(i));
                for (int j=0;j<12-daftarmakanan.get(i).length();j++){
                    System.out.print(" ");
                }
                System.out.print("|"+resep.get(i));
                for (int k=0;k<24-resep.get(i).length();k++){
                    System.out.print(" ");
                }
                System.out.println("|");
            }
            System.out.println("+---+------------+------------------------+");
            try{
                //memilih no makanan yang akan dimasak
                System.out.println("masukkan no makanan?");
                int noMakanan = Integer.parseInt(scan.nextLine());
                String makanan = "";
                for (int i=0;i<daftarmakanan.size();i++){
                    if (i==noMakanan-1){
                        makanan = daftarmakanan.get(i);
                    }
                }
                if (daftarmakanan.contains(makanan)){
                    currSim.masak(new Makanan(makanan));
                }else{
                    System.out.println("Barang tidak termasuk makanan yang dapat dimasak");
                } 
            }catch(NumberFormatException e){
                System.out.println("Masukan tidak valid");
            }
        }else{
            System.out.println("Sim tidak berada di kompor");
        }
    }

    //method untuk melakukan aksi buang air
    public void buangAir(){
        if (currSim.getPosisi().getCurrBarang().getNama().substring(0, 6).equals("Toilet")){
            currSim.buangAir();
        }else{
            System.out.println("Sim tidak berada di toilet");
        }
    }

    //method untuk melakukan aksi upgrade rumah
    public void upgradeHouse(){
        int j=0;
        Ruangan ruangan = currSim.getPosisi().getCurrRuangan();
        if (currSim.getRumah().getArrayOfRuangan().size()>1){
            for (Ruangan i : currSim.getRumah().getArrayOfRuangan()){
                System.out.println(j+1+". "+i.getNamaRuangan());
                j++;
            }
            //mengisi informasi terkait ruangan baru
            try{
                System.out.println("Masukkan no ruangan acuan");
                int no = Integer.parseInt(scan.nextLine());
                int k=0;
                for (Ruangan i : currSim.getRumah().getArrayOfRuangan()){
                    if (k==no-1){
                        ruangan = i;
                    }
                    k++;
                }
            }catch(NumberFormatException e){
                System.out.println("Gagal mendapat ruangan");
            }
        }else{
            ruangan=currSim.getPosisi().getCurrRuangan();
        }
        currSim.getPosisi().setCurrRuangan(ruangan);
        System.out.println("Masukkan nama ruangan");
        String nama = scan.nextLine();
        System.out.println("Di mana posisi dari ruangan saat ini");
        String orientasi = scan.nextLine();
        currSim.upgradeRumah(nama,orientasi);
    }

    //method untuk menampilkan waktu saat ini
    public void lihatWaktu(){
        if (currSim.getPosisi().getCurrBarang().getNama().substring(0, 3).equals("Jam")){
            currSim.lihatWaktu();
            System.out.println("Sisa waktu dalam progress :");
            // for (Ruangan i:currSim.getRumah().get)

            //menampilkan semua barang yang dibeli dan sedang diantar serta sisa waktu membangun rumah
            System.out.println();
            for (Barang i : currSim.getOnDelivery()){
                if (i instanceof NonMakanan){
                    NonMakanan nonmakanan = (NonMakanan)i; 
                    System.out.println(i.getNama()+": "+(nonmakanan.getWaktuSelesai()-world.getHari()*720-world.getWaktu()));
                }else{
                    BahanMakanan nonmakanan = (BahanMakanan)i; 
                    System.out.println(i.getNama()+": "+(nonmakanan.getWaktuSelesai()-world.getHari()*720-world.getWaktu()));
                }  
            }
            for (Ruangan i : currSim.getRumah().getRuanganBlomJadi()){
                System.out.println(i.getNamaRuangan()+": "+(i.getWaktuSelesai()-world.getHari()*720-world.getWaktu()));
            }
        }else{
            System.out.println("Sim tidak berada di Jam");
        }
    }

    //method untuk melakukan load game yang sebelumnya pernah disimpan
    public void load() throws FileNotFoundException{
        System.out.println("Masukkan namafile!");
        String namafile = scan.nextLine();
        String file = "src/main/resources/"+namafile+".json";
        try {
            reader.readWorld(world,file);
            currSim = world.getArrSim().get(0);
            for (Sim i : world.getArrSim()){
                //mengatur pemosisian barang untuk semua ruangan
                for (Ruangan k : i.getRumah().getArrayOfRuangan()){
                    for (Barang l: k.getBarangInRuangan()){
                        NonMakanan nonMakanan = (NonMakanan) l;
                        k.locateBarangLoad(nonMakanan,nonMakanan.getPosisi().getX(),nonMakanan.getPosisi().getY());
                    }
                }
                //mengatur ulang thread yang sebelumnya telah mati untuk barang yang sedang diantar dan ruangan yang sedang dibangun
                for (Barang j : i.getOnDelivery()){
                    try {
                        NonMakanan k = (NonMakanan) j;
                        TimerBarang timerbarang = new TimerBarang(k, i, k.getWaktuSelesai());
                        timerbarang.start();
                    }catch(ClassCastException e){
                        BahanMakanan k = (BahanMakanan) j;
                        TimerBarang timerbarang = new TimerBarang(k, i, k.getWaktuSelesai());
                        timerbarang.start();
                    }
                }
            }
        }catch (Exception e){

        }
    }

    //method untuk melakukan aksi terhadap benda yang dihadapi sim
    public void action(){
        try{
            if (currSim.getPosisi().getCurrBarang().getNama().equals("Kasur Single")){
                    tidur();
            }else if (currSim.getPosisi().getCurrBarang().getNama().equals("Kasur Queen Size")){
                    tidur();
            }else if (currSim.getPosisi().getCurrBarang().getNama().equals("Kasur King Size")){
                    tidur();
            }else if (currSim.getPosisi().getCurrBarang().getNama().equals("Toilet")){
                    buangAir();
            }else if (currSim.getPosisi().getCurrBarang().getNama().equals("Kompor Gas")){
                memasak();
                
            }else if (currSim.getPosisi().getCurrBarang().getNama().equals("Kompor Listrik")){
                memasak(); 
            }else if (currSim.getPosisi().getCurrBarang().getNama().equals("Meja dan Kursi")){
                makan(); 
            }else if (currSim.getPosisi().getCurrBarang().getNama().equals("Jam")){
                    lihatWaktu();
            }else if (currSim.getPosisi().getCurrBarang().getNama().equals("TV")){
                    nontonTV();
            }else if (currSim.getPosisi().getCurrBarang().getNama().equals("Laptop")){
                System.out.println("Berikut aksi yang dapat dilakukan");
                System.out.println("1. Ngoding");
                System.out.println("2. Dengar Musik");
                System.out.println("3. Main Game");
                System.out.println("4. Tidak Melakukan apa-apa");
                System.out.println("Masukkan angka!");
                try{
                    int no = Integer.parseInt(scan.nextLine());
                    if (no==1){
                        ngoding();
                    }else if(no==2){
                        dengarMusik();
                    }else if(no==3){
                        mainGame();
                    }
                }catch(NumberFormatException e){
                    System.out.println("Masukan tidak valid");
                }
            }
        }catch(NullPointerException e){
            System.out.println("Tidak ada aksi yang dapat dilakukan");
        }
    }

    //method untuk melakukan aksi makan
    public void makan(){
            if (currSim.getInventory().isEmpty()){
                System.out.println("Inventorymu kosong");
            }else{
                
                System.out.printf("|%-1s | %-10s | %-8s |%n","no", "makanan", "Jumlah");
            System.out.println("|---|------------|----------|");
            ArrayList<String> daftarMakanan = new ArrayList<>();
            ArrayList<String> daftarBahanMakanan = new ArrayList<>();
            daftarMakanan.add("Nasi Ayam");
            daftarMakanan.add("Nasi Kari");
            daftarMakanan.add("Susu Kacang");
            daftarMakanan.add("Tumis Sayur");
            daftarMakanan.add("Bistik");
            daftarMakanan.add("Nasi");
            daftarMakanan.add("Kentang");
            daftarMakanan.add("Ayam");
            daftarMakanan.add("Sapi");
            daftarMakanan.add("Wortel");
            daftarMakanan.add("Bayam");
            daftarMakanan.add("Kacang");
            daftarMakanan.add("Susu");
            int i=1;
            //display daftar makanan yang ada
            for (Map.Entry<String, Integer> entry : currSim.getInventory().entrySet()) {
                if (daftarMakanan.contains(entry.getKey())){
                    System.out.printf("| %-1d | %-10s | %-8d |%n", i,entry.getKey(),entry.getValue());
                    i++;
                }    
            }
            try{
                //pemilihan nomor makanan
                System.out.println("Masukkan nomor makanan");
                Integer noMakanan = Integer.parseInt(scan.nextLine());
                String namaMakanan = "";
                int k=0;
                int j=1;

                //searching makanan yang dipilih
                for (Map.Entry<String, Integer> entry : currSim.getInventory().entrySet()) {
                    if (j==noMakanan){
                        for (k=0;k<daftarMakanan.size();k++){
                            if (daftarMakanan.get(k).equals(entry.getKey())){
                                namaMakanan = entry.getKey();
                                break;
                            }
                        } 
                        break;
                    }else if (daftarMakanan.contains(entry.getKey())){
                        j++;
                    }    
                }
                if (k<5){
                    currSim.makan(new Makanan(namaMakanan));
                }else{
                    currSim.makan(new BahanMakanan(namaMakanan));
                }

            }catch(NumberFormatException e){
                System.out.println("Gagal melakukan makan");
            }
            }
            
    }

    
    //main program yang dijalankan
    public static void main(String[] args){
        
        MenuGame menu = new MenuGame();
        Display display = new Display();
        System.out.println("\t\t     Welcome To");
        display.home();
        while (true){
            System.out.println("silahkan memilih menu permainan");
            System.out.println("1. Start Game");
            System.out.println("2. Exit");
            System.out.println("3. Help");
            String command = menu.scan.nextLine();
            if (command.equals("Start Game")){
                menu.startGame();
            }else if (command.equals("Exit")){
                System.exit(0);
            }else if(command.equals("Help")){
                System.out.println("Berikut adalah panduan bermain game");
                System.out.println("Player akan memainkan satu orang sim di awal dan memilih titik lokasi rumah");
                System.out.println("Atribut-atribut yang dimiliki sim akan digenerate secara otomatis");
                System.out.println("Terdapat beberapa command yang dapat dijalankan oleh sim di dalam game");
                System.out.println("Game akan berakhir ketika semua sim yang ada mati");
            }else{
                System.out.println("Masukkan perintah command yang sesuai");
            }     
        }
    }
}
