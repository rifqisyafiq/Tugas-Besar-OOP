package simplicity;

import java.lang.*;

public class TimerBarang extends Thread {
    private int sisaWaktu;
    private Barang nonMakanan; // ini adalah object yang dibelinya
    private Sim sim; // sim merupakan sim yang melakukan pembeliannya
    private Object lock = World.getInstance().getLock();
    private int waktuBeres = -1;

    // kosntruktor
    public TimerBarang(Barang barang, Sim sim) {
        this.nonMakanan = barang;
        this.sim = sim;
        if (nonMakanan instanceof NonMakanan) {
            NonMakanan n = (NonMakanan) this.nonMakanan;
            this.sisaWaktu = n.getShippingTime();
        } else if (nonMakanan instanceof BahanMakanan) {
            BahanMakanan bahan = (BahanMakanan) this.nonMakanan;
            this.sisaWaktu = bahan.getShippingTime();
        }
    }

    public TimerBarang(Barang barang, Sim sim, int waktuSelesai) {
        // untuk load
        this.nonMakanan = barang;
        this.sim = sim;
        if (nonMakanan instanceof NonMakanan) {
            NonMakanan n = (NonMakanan) this.nonMakanan;
            this.sisaWaktu = n.getShippingTime();
        } else if (nonMakanan instanceof BahanMakanan) {
            BahanMakanan bahan = (BahanMakanan) this.nonMakanan;
            this.sisaWaktu = bahan.getShippingTime();
        }
        this.waktuBeres = waktuSelesai;
    }

    // run
    public void run() {
        World instance = World.getInstance();
        int waktuSelesai = instance.getHari() * 720 + instance.getWaktu() + sisaWaktu;
        boolean muter = true;
        // masukin dulu ke dalem on delivery
        Barang b = nonMakanan;
        if (this.waktuBeres != -1) {
            // load
            waktuSelesai = this.waktuBeres;
        } else {
            sim.getOnDelivery().add(b);
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
        // baru pindahin dari on delivery ke barang

        // hapus dari onDelivery

        synchronized (sim.getOnDelivery()) {
            sim.getOnDelivery().remove(b);
        }

        // ...

        // masukkin ke inventory
        synchronized (sim.getInventory()) {
            // masukkin ke inventory
            int jumlahSebelumnya;
            String namaBarang = b.getNama();

            if (sim.getInventory().containsKey(namaBarang)) {
                jumlahSebelumnya = sim.getInventory().get(namaBarang);
            } else {
                jumlahSebelumnya = 0;
            }
            sim.getInventory().put(namaBarang, jumlahSebelumnya + 1);
            // ...
            // barang sudah masuk inventory
            System.out.println("Selamat!, barang " + namaBarang + " yang Anda pesan sudah datang, silakan cek inventory");
        }

    }
}