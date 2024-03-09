package dev.nyon.skylper.skyblock.mining.hollows.solvers.metaldetector

interface MetalDetectorState {
    object Idle : MetalDetectorState
    object Solving : MetalDetectorState
}