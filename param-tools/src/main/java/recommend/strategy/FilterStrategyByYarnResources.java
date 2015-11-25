package recommend.strategy;

import historydb.App;

public class FilterStrategyByYarnResources {
    private static float MEM_DISCREPANCY_RATE = 0.3f;
    private static float VCORE_DISCREPANCY_RATE = 0.3f;
    private static float NODES_DISCREPANCY_RATE = 0.3f;

    public static boolean matched(App origin, App target) {
        Integer origin_totalMB = Integer.parseInt(origin.getYarnEnv().get("totalmb"));
        Integer origin_totalvcores = Integer.parseInt(origin.getYarnEnv().get("totalvirtualcores"));
        Integer origin_totalNodes = Integer.parseInt(origin.getYarnEnv().get("totalnodes"));

        Integer target_totalMB = Integer.parseInt(target.getYarnEnv().get("totalmb"));
        Integer target_totalvcores = Integer.parseInt(target.getYarnEnv().get("totalvirtualcores"));
        Integer target_totalNodes = Integer.parseInt(target.getYarnEnv().get("totalnodes"));

        return memoryMatch(origin_totalMB, target_totalMB) &&
                vcoreMatch(origin_totalvcores, target_totalvcores) &&
                nodeMatch(origin_totalNodes, target_totalNodes);
    }

    private static boolean memoryMatch(Integer origin, Integer target) {
        double frac = 1.0 * target / origin;
        return 1 - frac <= MEM_DISCREPANCY_RATE;
    }

    private static boolean vcoreMatch(Integer origin, Integer target) {
        double frac = 1.0 * target / origin;
        return 1 - frac <= VCORE_DISCREPANCY_RATE;
    }

    private static boolean nodeMatch(Integer origin, Integer target) {
        double frac = 1.0 * target / origin;
        return 1 - frac <= NODES_DISCREPANCY_RATE;
    }

}
