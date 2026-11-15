package id.ac.pens.psdku.campuslosthub.contract;

/** Kontrak untuk memeriksa bukti kepemilikan barang. */
public interface Verifiable {
    boolean verifyClaim(String proofData);
}
