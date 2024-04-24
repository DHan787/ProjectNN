# 图片的大小（n*n）
img_size_with_cuda = 512
img_size_without_cuda = 128

# 是否使用白噪声作为输入图片，一般会有噪点；若为0则使用content_image作为输入图片
noise_input = 1

# 训练步长，可以适当增加以便充分训练
num_steps = 300

# 风格和内容的权重，需要重点调试
style_weight = 1000000000
content_weight = 1

# 风格和内容在卷积层上的定义，可以调整内容层的位置
style_layers_default = ['conv_1', 'conv_5', 'conv_8', 'conv_13', 'conv_17']
content_layers_default = ['conv_2']


# content_layers_default = ['conv_4']

class Arguments:
    def __init__(self):
        self._style_weight = 1000000000

    @property
    def style_weight(self):
        return self._style_weight

    @style_weight.setter
    def style_weight(self, value):
        self._style_weight = value
