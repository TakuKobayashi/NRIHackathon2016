package hackathon.nri.com.nrihackathon2016;

import android.util.SparseArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FericaCollection extends ArrayList<FericaRecord>{
    /**
     * 履歴Felica応答の解析。
     * @param res Felica応答
     * @return 文字列表現
     * @throws Exception
     */
    public static FericaCollection parse(byte[] res) throws Exception {
        // res[0] = データ長
        // res[1] = 0x07
        // res[2〜9] = カードID
        // res[10,11] = エラーコード。0=正常。
        if (res[10] != 0x00) throw new RuntimeException("Felica error.");

        // res[12] = 応答ブロック数
        // res[13+n*16] = 履歴データ。16byte/ブロックの繰り返し。
        int size = res[12];
        FericaCollection fericaCollection = new FericaCollection();
        String str = "";
        for (int i = 0; i < size; i++) {
            // 個々の履歴の解析。
            FericaRecord fericaRecord = FericaRecord.parse(res, 13 + i * 16);
            fericaCollection.add(fericaRecord);
        }
        return fericaCollection;
    }
}
