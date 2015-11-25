package recommend.strategy;

import historydb.App;

public class FilterStrategyByDataset {
    private static float INPUT_LOG_DISCREPANCY_RATIO = 3;

    public static boolean matched(App origin, App target) {
        Double originSize = origin.getInputSizeMB();
        Double targetSize = target.getInputSizeMB();
        if (originSize == null || targetSize == null) return false;
        else if (originSize == 0L) return targetSize == 0L;
        else {
            double frac = targetSize / originSize;
            return Math.abs(Math.log(frac)) <= INPUT_LOG_DISCREPANCY_RATIO;
        }
    }

}
