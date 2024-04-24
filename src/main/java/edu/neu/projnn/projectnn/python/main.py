from __future__ import print_function

import sys

import matplotlib
import torch
import torchvision.models as models
import torchvision.transforms as transforms

import arguments as args
import classes as cls


def generate_image(style_weight):
    matplotlib.use('TkAgg')
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    imsize = args.img_size_with_cuda if torch.cuda.is_available() else args.img_size_without_cuda
    loader = transforms.Compose([
        transforms.Resize((imsize, imsize)),
        transforms.ToTensor()])
    content_img = cls.image_loader("D:\\ProjectNN\\src\\main\\resources\\uploaded\\original.jpg", loader, device)
    style_img = cls.image_loader("D:\\ProjectNN\\src\\main\\resources\\style\\style.jpg", loader, device)
    assert style_img.size() == content_img.size(), "请导入相同大小的样式和内容图像"
    unloader = transforms.ToPILImage()
    cnn = models.vgg19(pretrained=True).features.to(device).eval()
    cnn_normalization_mean = torch.tensor([0.485, 0.456, 0.406]).to(device)
    cnn_normalization_std = torch.tensor([0.229, 0.224, 0.225]).to(device)
    if args.noise_input == 1:
        input_img = content_img.clone()
    else:
        input_img = torch.randn(content_img.data.size(), device=device)
    output = cls.run_style_transfer(cnn, cnn_normalization_mean, cnn_normalization_std,
                                    content_img, style_img, input_img, style_weight, device)
    single_image = output[0].cpu().clone().clamp(0, 1)
    output_img = unloader(single_image.squeeze())
    output_img.save("D:\\ProjectNN\\src\\main\\resources\\static\\generated\\generated.jpg")


if __name__ == "__main__":
    if len(sys.argv) > 1:
        style_weight = float(sys.argv[1])
        generate_image(style_weight)
    else:
        print("No style weight provided")
