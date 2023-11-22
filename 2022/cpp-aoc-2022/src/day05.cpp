#include "day04.hpp"

#include <iostream>

#include <ranges>

#include "util.hpp"

struct Command {
    int num;
    int from;
    int to;

    void execute(std::vector<std::vector<char>>& stacks) const {
        for (int i = 0; i < num; i++) {
            char c = stacks[from].back();
            stacks[from].pop_back();
            stacks[to].push_back(c);
        }
    }

    friend std::istream &operator>>(std::istream &stream, Command &command) {
        std::string _;

        stream >> _ >> command.num >> _ >> command.from >> _ >> command.to;

        command.from--;
        command.to--;

        return stream;
    }
};

std::string day05_part1(std::istream &input) {
    std::string line;
    std::vector<std::vector<char>> stacks;
    while (std::getline(input, line)) {
        if (std::any_of(line.begin(), line.end(), [](char c){return std::isdigit(c);})) {
            break;
        }

        for (int i = 1; i < line.length(); i += 4) {
            int stack_index = (i - 1) / 4;
            if (stack_index >= stacks.size()) {
                stacks.resize(stack_index + 1);
            }

            char c = line[i];
            if (std::isupper(c)) {
                stacks[stack_index].push_back(line[i]);
            }
        }
    }

    std::cout << "thar stacks" << std::endl;
    for (auto &stack: stacks) {
        std::reverse(stack.begin(), stack.end());

        for (const auto &item: stack) {
            std::cout << item;
        }
        std::cout << std::endl;
    }

    // consume blank line
    std::getline(input, line);

    std::vector<Command> commands;
    std::ranges::copy(std::views::istream<Command>(input), std::back_inserter(commands));

    for (const auto &command: commands) {
        command.execute(stacks);
    }

    std::string result;
    for (const auto &stack: stacks) {
        result += stack.back();
    }

    for (auto &stack: stacks) {
        std::reverse(stack.begin(), stack.end());

        for (const auto &item: stack) {
            std::cout << item;
        }
        std::cout << std::endl;
    }


    return result;
}

std::string day05_part2(const std::istream &input) {
    return "cheese";
}
