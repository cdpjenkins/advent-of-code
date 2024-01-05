#include <vector>
#include <string>
#include <ranges>
#include <algorithm>
#include <numeric>
#include <cstdint>
#include <iostream>
#include <regex>

int main() {
    using namespace std::string_literals;

    const std::vector<std::string> input{
            "1000",
            "2000",
            "3000",
            "",
            "4000",
            "",
            "5000",
            "6000",
            "",
            "7000",
            "8000",
            "9000",
            "",
            "10000"
    };

    std::regex regex(R"(^(\d+)$)");

    // Split the string using std::views::split
    auto elves = input
            | std::views::lazy_split(""s)
            | std::views::transform([&regex](const auto& elf) -> uint64_t {
                int cnt = 0;
                std::smatch match;
                for (auto& line : elf) {
                    if (std::regex_match(line, match, regex)) {
                        std::cout << "thar thang is " << match[1] << std::endl;

                        auto& [_, ston, stour, poo, wee, foo] = match;

                        cnt += std::stoi(match[1]);
                    }
                }
                return cnt;
            });

    std::vector<int> ston{elves.begin(), elves.end()};

    for (const auto &item: ston) {
        std::cout << item << std::endl;
    }

    // std::cout << std::endl << std::endl;

    // std::cout << std::ranges::max(elves);

    return 0;
}
