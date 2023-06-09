package simplicity;

import java.util.ArrayList;

public class TimerRumah extends Thread {
    private Sim sim;
    private String namaRuangan;
    private Ruangan ruanganSekarang;
    private String lokasi;
    private Object lock = World.getInstance().getLock();
    private int waktuBeres = -1;

    public TimerRumah(Sim sim, String namaRuangan, Ruangan ruanganSekarang, String lokasi) {
        this.sim = sim;
        this.namaRuangan = namaRuangan;
        this.ruanganSekarang = ruanganSekarang;
        this.lokasi = lokasi;
    }

    public TimerRumah(Sim sim, String namaRuangan, Ruangan ruanganSekarang, String lokasi, int waktuSelesai) {
        // untuk load
        this.sim = sim;
        this.namaRuangan = namaRuangan;
        this.ruanganSekarang = ruanganSekarang;
        this.lokasi = lokasi;
        this.waktuBeres = waktuSelesai;
    }

    public void run() {
        if(lokasi.equals("kanan") || lokasi.equals("kiri") || lokasi.equals("atas")|| lokasi.equals("bawah")){
            // lokasinya valid    
            World instance = World.getInstance();
            int waktuSelesai = instance.getHari() * 720 + instance.getWaktu() + 1080;
            boolean muter = true;
            Ruangan ruangBaru = new Ruangan(namaRuangan);
            ruangBaru.setWaktuSelesai(waktuSelesai);
            if (waktuBeres != -1) {
                // load
                waktuSelesai = this.waktuBeres;
            } else {
                // masukin dulu ke denah sehingga saat proses tidak dapat ditimpa
                if (lokasi.equals("kanan")) {
                    if (ruanganSekarang.getRuangKanan() == null) {
                        ruanganSekarang.setRuangKanan(ruangBaru);
                        ruangBaru.setRuangKiri(ruanganSekarang);
                        // menambahkan ruangan di ruangan blom jadi
                        this.sim.getRumah().addRuanganBlomJadi(ruangBaru);
                    } else {
                        System.out.println("Di sebelah kanan sudah ada ruangan");
                        return;
                    }
    
                } else if (lokasi.equals("kiri")) {
                    if (ruanganSekarang.getRuangKiri() == null) {
                        ruanganSekarang.setRuangKiri(ruangBaru);
                        ruangBaru.setRuangKanan(ruanganSekarang);
                        // menambahkan ruangan di ruangan blom jadi
                        this.sim.getRumah().addRuanganBlomJadi(ruangBaru);
                    } else {
                        System.out.println("Di sebelah kiri sudah ada ruangan");
                        return;
                    }
                } else if (lokasi.equals("atas")) {
                    if (ruanganSekarang.getRuangAtas() == null) {
                        ruanganSekarang.setRuangAtas(ruangBaru);
                        ruangBaru.setRuangBawah(ruanganSekarang);
                        // menambahkan ruangan di ruangan blom jadi
                        this.sim.getRumah().addRuanganBlomJadi(ruangBaru);
                    } else {
                        System.out.println("Di atas sudah ada ruangan");
                        return;
                    }
                } else if (lokasi.equals("bawah")) {
                    if (ruanganSekarang.getRuangBawah() == null) {
                        ruanganSekarang.setRuangBawah(ruangBaru);
                        ruangBaru.setRuangAtas(ruanganSekarang);
                        // menambahkan ruangan di ruangan blom jadi
                        this.sim.getRumah().addRuanganBlomJadi(ruangBaru);
                    } else {
                        System.out.println("Di bawah sudah ada ruangan");
                        return;
                    }
                }
            }
            while (muter) {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        System.err.println(e);
                    }
                }
                // mengecek jam
                if (instance.getHari() * 720 + instance.getWaktu() >= waktuSelesai) {
                    muter = false;
                }
            }
    
            // ilangin dari array Ruangan Blom jadi
            this.sim.getRumah().deleteRuanganBlomJadi(ruangBaru);
    
            // baru tambahin ruangan ke dalam rumah
            this.sim.getRumah().addRuangan(ruangBaru);
        }
        else{
            // lokasi tidak valid
            System.out.println("Letak ruangan tidak valid!");
        }
    }

}