# Instalación de Java 21 en Linux

A continuación se presentan los comandos para instalar el JDK de Java 21 en las distintas distribuciones de Linux.

## Ubuntu / Debian (y derivados como Linux Mint, Pop!_OS)

```bash
sudo apt update && sudo apt install openjdk-21-jdk -y
```

## Fedora / RHEL / AlmaLinux / Rocky Linux

```bash
sudo dnf install java-21-openjdk-devel -y
```

## Arch Linux / Artix Linux / Manjaro

```bash
sudo pacman -S jdk21-openjdk
```

## openSUSE

```bash
sudo zypper install java-21-openjdk-devel
```
