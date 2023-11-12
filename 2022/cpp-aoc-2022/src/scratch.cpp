#include <vector>
#include <string>
#include <ranges>
#include <algorithm>
#include <numeric>
#include <cstdint>
#include <iostream>

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

    // Split the string using std::views::split
    auto elves = input
            | std::views::lazy_split(""s)
            | std::views::transform([](const auto& elf) -> uint64_t { // sum up the calories for each elf: range{uint64_t}
                  auto to_unsigned = [](const auto& in) { return std::stoull(in); };
                  auto rng = elf | std::views::transform(to_unsigned) | std::views::common; // range{string} -> range{uint64_t}
                  return std::reduce(rng.begin(), rng.end()); // range{uint64_t} -> uint64_t
              });

    std::vector<int> ston{elves.begin(), elves.end()};

    for (const auto &item: ston) {
        std::cout << item << std::endl;
    }

    std::cout << std::endl << std::endl;

    std::cout << std::ranges::max(elves);

    return 0;
}
