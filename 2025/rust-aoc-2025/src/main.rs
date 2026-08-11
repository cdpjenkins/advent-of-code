use std::path::Component::ParentDir;
use std::str::Lines;

fn main() {
    let result = std::fs::read_to_string("../../advent-of-code-input/2025/day01.txt");

    if let Ok(input) = result {
        let lines = input.lines();

        let times_at_0 = count_times_dial_stops_at_zero(lines);

        println!("times_at_0: {}", times_at_0);
    } else {
        eprintln!("Unable to read file!!!1");
    }
}

fn count_times_dial_stops_at_zero(lines: Lines) -> i32 {
    let instructions = lines.map(|line| { to_instruction(line) });

    let mut pos = 50;
    let mut times_at_0 = 0;
    for instruction in instructions {
        pos += instruction;
        println!("position: {} instruction {:?}", pos, instruction);

        if pos.rem_euclid(100) == 0 {
            println!("We is at 0!");
            times_at_0 += 1;
        }
    }
    times_at_0
}

fn to_instruction(line: &str) -> i32 {
    let direction = if line.starts_with('L') {-1} else {1};

    let rest = &line[1..];
    let magnitude = rest.parse::<i32>().unwrap();

    magnitude * direction
}

mod tests {
    use crate::{count_times_dial_stops_at_zero, to_instruction};

    #[test]
    fn parses_an_input_line_correctly() {
        assert_eq!(12, to_instruction("R12"));
        assert_eq!(-34, to_instruction("L34"));
    }

    #[test]
    fn part_1_test_input() {
        let input = r#"L68
L30
R48
L5
R60
L55
L1
L99
R14
L82"#.lines();

        let result = count_times_dial_stops_at_zero(input);

        assert_eq!(3, result);
    }

    #[test]
    fn part_1_real_input() {
        let input_string = std::fs::read_to_string("../../advent-of-code-input/2025/day01.txt").unwrap();
        let input = input_string.lines();

        let result = count_times_dial_stops_at_zero(input);

        assert_eq!(1084, result);
    }
}
