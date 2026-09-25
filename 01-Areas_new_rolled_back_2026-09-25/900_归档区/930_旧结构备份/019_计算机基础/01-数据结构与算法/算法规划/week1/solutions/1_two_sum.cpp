#include <iostream>
#include <vector>
#include <unordered_map>
using namespace std;

vector<int> twoSum(vector<int>& nums, int target) {
    unordered_map<int,int> mp; // value -> index
    for (int i = 0; i < (int)nums.size(); ++i) {
        int need = target - nums[i];
        auto it = mp.find(need);
        if (it != mp.end()) return {it->second, i};
        mp[nums[i]] = i;
    }
    return {};
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    vector<int> nums = {2,7,11,15};
    int target = 9;
    auto res = twoSum(nums, target);
    if (res.empty()) {
        cout << "No solution\n";
    } else {
        cout << "indices: ";
        for (int x : res) cout << x << " ";
        cout << '\n';
    }
    return 0;
}
