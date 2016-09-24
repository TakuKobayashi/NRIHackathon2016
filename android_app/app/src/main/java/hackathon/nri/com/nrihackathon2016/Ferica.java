package hackathon.nri.com.nrihackathon2016;

import android.util.SparseArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Ferica {
    /**
     * 履歴読み込みFelicaコマンドの取得。
     * - Sonyの「Felicaユーザマニュアル抜粋」の仕様から。
     * - サービスコードは http://sourceforge.jp/projects/felicalib/wiki/suica の情報から
     * - 取得できる履歴数の上限は「製品により異なります」。
     * @param idm カードのID
     * @param size 取得する履歴の数
     * @return Felicaコマンド
     * @throws IOException
     */
    public static byte[] readWithoutEncryption(byte[] idm, int size)
            throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream(100);

        bout.write(0);           // データ長バイトのダミー
        bout.write(0x06);        // Felicaコマンド「Read Without Encryption」
        bout.write(idm);         // カードID 8byte
        bout.write(1);           // サービスコードリストの長さ(以下２バイトがこの数分繰り返す)
        bout.write(0x0f);        // 履歴のサービスコード下位バイト
        bout.write(0x09);        // 履歴のサービスコード上位バイト
        bout.write(size);        // ブロック数
        for (int i = 0; i < size; i++) {
            bout.write(0x80);    // ブロックエレメント上位バイト 「Felicaユーザマニュアル抜粋」の4.3項参照
            bout.write(i);       // ブロック番号
        }

        byte[] msg = bout.toByteArray();
        msg[0] = (byte) msg.length; // 先頭１バイトはデータ長
        return msg;
    }
}
