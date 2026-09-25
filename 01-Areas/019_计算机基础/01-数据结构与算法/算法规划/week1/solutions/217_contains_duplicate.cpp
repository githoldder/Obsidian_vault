#include <iostream>
#include <vector>
#include <unordered_set>
using namespace std;

bool containsDuplicate(vector<int>& nums) {
    unordered_set<int> s;
    for (int x : nums) {
        if (s.find(x) != s.end()) return true;
        s.insert(x);
    }
    return false;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    vector<int> nums = {1,2,3,1};
    cout << (containsDuplicate(nums) ? "true" : "false") << '\n';
    vector<int> nums2 = {1,2,3,4};
    cout << (containsDuplicate(nums2) ? "true" : "false") << '\n';
    return 0;
}
