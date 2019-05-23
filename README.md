# Metamer

[![Codacy Badge][codacy_badge]][codacy_link]
[![Build Status][travis_badge]][travis_link]
[![License: MIT][license_badge]][license_link]
[![Contributor Covenant][coc_badge]](CODE-OF-CONDUCT.md)

> Genome assembler de novo that uses De Bruijn graph.

## Table of Contents

-   [Install](#install)
    -   [Dependencies](#dependencies)
-   [Usage](#usage)
    -   [Options](#options)
    -   [Examples](#examples)
        -   [General use cases](#general-use-cases)
        -   [Pipeline mode](#pipeline-mode)
-   [Maintainer](#maintainer)
-   [Contributing](#contributing)
    -   [Contributors](#contributors)
-   [License](#license)

## Install

Please, visit the [releases page][releases_link] to download the tool. 

### Dependencies

-   `Java 11`

## Usage

    java -jar metamer.jar [-h | --help] [-k <number> -f <format> -i <path> -o <path>]

### Options

`-h`, `--help` - show usage.

`-k` - the length of `k` that will be used to build De Bruijn graph.

`-f`, `--format` - the format of input data that will be used to read records, can be one of: `fasta`, `fastq`.

`-i`, `--input` - input file with reads to be analyzed.

`-o`, `--output` - output file to write the result to.

### Examples

#### General use cases

Use `Fasta` file as input: 

```bash
java -jar metamer.jar -k 42 -f fasta -i ~/path/to/input.fasta -o ~/path/to/output.fasta
```

Use `FastQ` file as input:

```bash
java -jar metamer.jar -k 42 -f fastq -i ~/path/to/input.fastq -o ~/path/to/output.fasta
```

#### Pipeline mode

For all examples below used `fasta` format but it can be used with `fastq` in the exact same way as well.

Read from `stdin` and write to `stdout`:

```bash
java -jar metamer.jar -k 42 -f fasta
```

Read from file and write to `stdout`:

```bash
java -jar metamer.jar -k 42 -f fasta -i ~/path/to/input.fasta
```

Read from `stdin` and write to a file:

```bash
java -jar metamer.jar -k 42 -f fasta -o ~/path/to/output.fasta
```

## Maintainer

[Denis Verkhoturov](https://github.com/DenisVerkhoturov)

## Contributing

[Issues](issues) and [pull-requests](pulls) are appreciated, thank you for paying attention to project Metamer!
Please take a look at our [code of conduct](CODE-OF-CONDUCT.md) and [contributing guide](CONTRIBUTING.md).

### Contributors

-   [Aleksandra Klimina](https://github.com/alex-klimina)
-   [Anna Brusnitsyna](https://github.com/AnBrusn)
-   [Denis Verkhoturov](https://github.com/DenisVerkhoturov)
-   [Irina Shapovalova](https://github.com/ShapovalovaIrina)
-   [Sophia Shalgueva](https://github.com/Sonik-zirael)

## License

This project is licenced under the terms of the [MIT](LICENSE) license.

[codacy_link]: https://www.codacy.com/app/metamer/metamer?utm_source=github.com&utm_medium=referral&utm_content=DenisVerkhoturov/metamer&utm_campaign=Badge_Grade

[codacy_badge]: https://api.codacy.com/project/badge/Grade/01b961320fd848d2bde13d60656e74b3

[travis_link]: https://travis-ci.org/DenisVerkhoturov/metamer

[travis_badge]: https://travis-ci.org/DenisVerkhoturov/metamer.svg?branch=master

[license_link]: https://opensource.org/licenses/MIT

[license_badge]: https://img.shields.io/badge/License-MIT-blue.svg

[coc_badge]: https://img.shields.io/badge/Contributor%20Covenant-v1.4%20adopted-ff69b4.svg

[releases_link]: https://github.com/DenisVerkhoturov/metamer/releases
