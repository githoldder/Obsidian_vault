#include <iostream>
#include <string>
#include <array>
using namespace std;

bool isAnagram(const string &s, const string &t) {
    if (s.size() != t.size()) return false;
    array<int, 26> cnt{};
    for (char c : s) cnt[c - 'a']++;
    for (char c : t) cnt[c - 'a']--;
    for (int x : cnt) if (x != 0) return false;
    return true;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    cout << (isAnagram("anagram","nagaram") ? "true" : "false") << '\n';
    cout << (isAnagram("rat","car") ? "true" : "false") << '\n';
    return 0;
}
